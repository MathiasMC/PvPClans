package me.MathiasMC.PvPClans.api;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class ClanAPI {

    private static ClanAPI instance;

    private final PvPClans plugin;

    public ClanAPI() {
        this.plugin = PvPClans.getInstance();
    }

    public ClanPlayer getClanPlayer(UUID uuid) {
        return plugin.getClanPlayer(uuid);
    }

    public Clan getClan(long clanID) {
        return plugin.getClan(clanID);
    }

    public Clan getClan(UUID uuid) {
        return plugin.getClan(uuid);
    }

    public Map<Long, Clan> getClans() {
        return plugin.getClans();
    }

    public ItemStack getPlayerHead(UUID uuid) {
        return plugin.getItemManager().getHeadAsync(uuid);
    }

    public boolean createClan(ClanPlayer clanPlayer, String name) {
        if (clanPlayer.getClanID() == 0) return false;
        plugin.database.insertClan(clanPlayer.getUniqueId(), plugin.getNewID(), name);
        return true;
    }

    public void deleteClan(Clan clan) {
        plugin.database.deleteClan(clan);
    }

    public static ClanAPI getInstance() {
        if (instance == null) {
            instance = new ClanAPI();
        }
        return instance;
    }
}
