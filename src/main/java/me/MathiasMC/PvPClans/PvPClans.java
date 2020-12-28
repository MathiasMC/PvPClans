package me.MathiasMC.PvPClans;

import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.commands.PvPClans_Command;
import me.MathiasMC.PvPClans.commands.PvPClans_TabComplete;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.Database;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.Purge;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.listeners.*;
import me.MathiasMC.PvPClans.managers.*;
import me.MathiasMC.PvPClans.perks.CoinsBonusPerk;
import me.MathiasMC.PvPClans.perks.DamagePerk;
import me.MathiasMC.PvPClans.perks.ExplosiveArrowPerk;
import me.MathiasMC.PvPClans.perks.XpBonusPerk;
import me.MathiasMC.PvPClans.support.PlaceholderAPI;
import me.MathiasMC.PvPClans.task.SaveTask;
import me.MathiasMC.PvPClans.utils.FileUtils;
import me.MathiasMC.PvPClans.utils.Metrics;
import me.MathiasMC.PvPClans.utils.UpdateUtils;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class PvPClans extends JavaPlugin {

    private static PvPClans call;

    public Database database;

    private FileUtils fileUtils;

    private StatsManager statsManager;

    private ItemManager itemManager;

    private SessionManager sessionManager;

    private ClanManager clanManager;

    private final Map<UUID, ClanPlayer> clanPlayer = new HashMap<>();
    private final Map<Long, Clan> clan = new HashMap<>();
    public final LinkedHashSet<String> clanNames = new LinkedHashSet<>();

    private final HashMap<Player, Menu> playerMenu = new HashMap<>();

    private final HashMap<UUID, UUID> invites = new HashMap<>();

    private final LinkedHashSet<UUID> pendingDelete = new LinkedHashSet<>();

    private final Map<UUID, ItemStack> playerHeads = new HashMap<>();

    private final LinkedHashSet<UUID> renameClan = new LinkedHashSet<>();

    private final Map<String, Perk> perks = new HashMap<>();

    private SaveTask saveTask;

    private final Random random = new Random();

    private final Pattern bracket = Pattern.compile("[{]([^{}]+)[}]");

    public void onEnable() {
        call = this;
        itemManager = new ItemManager(this);
        fileUtils = new FileUtils(this);
        database = new Database(this);
        statsManager = new StatsManager(this);
        sessionManager = new SessionManager(this);
        clanManager = new ClanManager(this);
        if (database.set()) {
            getServer().getPluginManager().registerEvents(new PlayerLogin(this), this);
            getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
            getServer().getPluginManager().registerEvents(new InventoryClick(), this);
            getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
            getServer().getPluginManager().registerEvents(new AsyncPlayerChat(this), this);
            ArrayList<Long> clans = database.getClans();
            for (long id : clans) {
                Clan clan = getClan(id);
                clanNames.add(clan.getName());
            }
            Utils.info("Loaded ( " + clans.size() + " ) clans");
            int minutes = fileUtils.config.getInt("mysql.save", 10) * 1200;

            saveTask = new SaveTask(this);
            saveTask.runTaskTimerAsynchronously(this, minutes, minutes);

            getCommand("pvpclans").setExecutor(new PvPClans_Command(this));
            getCommand("pvpclans").setTabCompleter(new PvPClans_TabComplete(this));

            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new PlaceholderAPI(this).register();
            }

            if (fileUtils.config.contains("mysql.purge")) {
                new Purge(this);
            }
            if (fileUtils.config.getBoolean("perks", true)) {
                getServer().getPluginManager().registerEvents(new EntityDamageByEntity(this), this);
                getServer().getPluginManager().registerEvents(new ProjectileHit(this), this);
                new CoinsBonusPerk().register();
                new DamagePerk().register();
                new ExplosiveArrowPerk().register();
                new XpBonusPerk().register();
            }

            Utils.info("Created by MathiasMC");
            Utils.info(" ");

            Metrics metrics = new Metrics(this, 9629);
            metrics.addCustomChart(new Metrics.SimplePie("clans", () -> String.valueOf(clans.size())));

            if (fileUtils.config.getBoolean("update-check")) {
                new UpdateUtils(this, 87180).getVersion(version -> {
                    if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                        Utils.info("You are using the latest version of PvPClans (" + getDescription().getVersion() + ")");
                    } else {
                        Utils.warning("Version: " + version + " has been released! you are currently using version: " + getDescription().getVersion());
                    }
                });
            }

        } else {
            Utils.error("Disabling plugin cannot connect to database");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void onDisable() {
        try {
            database.close();
        } catch (SQLException exception) {
            Utils.exception(exception.getStackTrace(), exception.getMessage());
        }
        call = null;
    }

    public static PvPClans getInstance() {
        return call;
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public SaveTask getSaveTask() {
        return saveTask;
    }

    public HashMap<UUID, UUID> getInvites() {
        return invites;
    }

    public LinkedHashSet<UUID> getPendingDelete() {
        return pendingDelete;
    }

    public Map<UUID, ItemStack> getPlayerHeads() {
        return playerHeads;
    }

    public LinkedHashSet<UUID> getRenameClan() {
        return renameClan;
    }

    public ClanPlayer getClanPlayer(final UUID playerUUID) {
        if (clanPlayer.containsKey(playerUUID)) {
            return clanPlayer.get(playerUUID);
        }
        final ClanPlayer clanPlayer = new ClanPlayer(playerUUID);
        this.clanPlayer.put(playerUUID, clanPlayer);
        return clanPlayer;
    }

    public HashMap<Player, Menu> getPlayerMenu() {
        return playerMenu;
    }

    public void removeClan(final long clanID) {
        this.clan.remove(clanID);
    }

    public Clan getClan(final long clanID) {
        if (clan.containsKey(clanID)) {
            return clan.get(clanID);
        }
        if (clanID == 0) return null;
        final Clan clan = new Clan(clanID);
        this.clan.put(clanID, clan);
        return clan;
    }

    public Clan getClan(UUID uuid) {
        return getClanPlayer(uuid).getClan();
    }

    public Map<Long, Clan> getClans() {
        return clan;
    }

    public long getNewID() {
        for (long i = 1; i <= (clan.size() + 1); i++) {
            if (!clan.containsKey(i)) {
                return i;
            }
        }
        return 1;
    }

    public Menu getPlayerMenu(Player player) {
        Menu playerMenu;
        if (!this.playerMenu.containsKey(player)) {
            playerMenu = new Menu(player);
            this.playerMenu.put(player, playerMenu);
            return playerMenu;
        } else {
            return this.playerMenu.get(player);
        }
    }

    public Random getRandom() {
        return random;
    }

    public Pattern getBracket() {
        return bracket;
    }

    public Map<String, Perk> getPerks() {
        return perks;
    }
}
