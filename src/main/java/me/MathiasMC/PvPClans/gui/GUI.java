package me.MathiasMC.PvPClans.gui;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.ClanStats;
import me.MathiasMC.PvPClans.gui.menu.*;
import me.MathiasMC.PvPClans.gui.menu.KickGUI;
import me.MathiasMC.PvPClans.gui.menu.MemberGUI;
import me.MathiasMC.PvPClans.gui.menu.ModeratorGUI;
import me.MathiasMC.PvPClans.gui.menu.market.MemberSizeGUI;
import me.MathiasMC.PvPClans.gui.menu.market.RenameGUI;
import me.MathiasMC.PvPClans.gui.menu.market.MarketGUI;
import me.MathiasMC.PvPClans.gui.menu.market.UpgradeGUI;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public abstract class GUI implements InventoryHolder {

    protected final PvPClans plugin;

    protected final ClanPlayer clanPlayer;

    protected final UUID uuid;

    protected final Menu playerMenu;
    protected final FileConfiguration file;

    protected Inventory inventory;

    protected int page = 0;

    protected int index = 0;

    protected int size = 0;

    public void setItems() {

    }

    public boolean perform(String text) {
        return false;
    }

    public GUI(Menu playerMenu) {
        this.plugin = PvPClans.getInstance();
        this.uuid = playerMenu.getUniqueId();
        this.clanPlayer = plugin.getClanPlayer(uuid);
        this.playerMenu = playerMenu;
        this.file = this.getFile();
    }

    public void open() {
        playerMenu.getPlayers().clear();
        playerMenu.getPerks().clear();
        inventory = plugin.getServer().createInventory(this, getSize(), getName());
        this.setItemsEvent();
        playerMenu.getPlayer().openInventory(inventory);
    }

    public abstract FileConfiguration getFile();

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return Utils.replacePlaceholders(clanPlayer, file.getString("title"));
    }

    public int getSize() {
        return file.getInt("size");
    }

    public void click(InventoryClickEvent e) {
        int slot = e.getSlot();
        String type = "left";
        if (e.isRightClick()) {
            type = "right";
            if (playerMenu.getPlayers().containsKey(slot)) {
                UUID clickUUID = playerMenu.getPlayers().get(slot);
                ClanPlayer clanTarget = plugin.getClanPlayer(clickUUID);
                Clan clan = clanPlayer.getClan();
                playerMenu.setClanTarget(clanTarget);
                if (!clan.isModerator(uuid) && !clan.isLeader(uuid)) {
                    Utils.dispatchCommandList(clanPlayer, clanTarget, "gui.kick.rank");
                    return;
                }
                if (clickUUID.equals(clan.getLeader()) || clanPlayer.equals(clanTarget)) {
                    Utils.dispatchCommandList(clanPlayer, clanTarget, "gui.kick.cannot");
                    return;
                }
                new KickGUI(playerMenu).open();
                return;
            }
        } else if (e.isLeftClick()) {
            if (playerMenu.getPlayers().containsKey(slot)) {
                UUID clickUUID = playerMenu.getPlayers().get(slot);
                ClanPlayer clanTarget = plugin.getClanPlayer(clickUUID);
                Clan clan = clanPlayer.getClan();
                playerMenu.setClanTarget(clanTarget);
                if (!clan.isLeader(uuid)) {
                    Utils.dispatchCommandList(clanPlayer, clanTarget, "gui.moderator.rank");
                    return;
                }
                if (clan.isLeader(clickUUID)) {
                    Utils.dispatchCommandList(clanPlayer, clanTarget, "gui.moderator.cannot");
                    return;
                }
                if (clan.isModerator(clickUUID)) {
                    new MemberGUI(playerMenu).open();
                    return;
                }
                new ModeratorGUI(playerMenu).open();
                return;
            } else if (playerMenu.getPerks().containsKey(slot)) {
                Clan clan = clanPlayer.getClan();
                if (!clan.isLeader(uuid) && !clan.isModerator(uuid)) {
                    Utils.dispatchCommandList(clanPlayer,  "gui.perks.rank");
                    return;
                }
                clan.setPerk(playerMenu.getPerks().get(slot), !clan.isPerkActive(playerMenu.getPerks().get(slot)));
                open();
            }
        }
        String path = "items." + slot + "." + type;
        if (!file.contains(path)) return;
        for (String performType : file.getStringList(path + ".perform")) {
            PerformType match = PerformType.get(performType);
            if (match == null) return;
            match(match, performType.substring(match.getIdentifier().length()).trim());
        }
    }

    public void setItemsEvent() {
        plugin.getItemManager().setItems(clanPlayer, inventory, file);
        this.setItems();
    }

    public void match(PerformType match, String text) {
        switch (match) {
            case GUI:
                switch (text) {
                    case "market":
                        if (!clanPlayer.getClan().isLeader(uuid)) return;
                        new MarketGUI(playerMenu).open();
                        break;
                    case "upgrades":
                        new UpgradesGUI(playerMenu).open();
                        break;
                    case "perks":
                        new PerksGUI(playerMenu).open();
                        break;
                    case "clans":
                        new ClansGUI(playerMenu).open();
                        break;
                    case "clan":
                        new ClanGUI(playerMenu).open();
                        break;
                    case "market_rename":
                        new RenameGUI(playerMenu).open();
                        break;
                    case "market_member_size":
                        new MemberSizeGUI(playerMenu).open();
                        break;
                    case "market_upgrade":
                        new UpgradeGUI(playerMenu).open();
                        break;
                }
                break;
            case CONFIRM:
                if (perform(text)) {
                    PerformType performType = PerformType.get(text);
                    if (performType == null) return;
                    match(performType, text.substring(performType.getIdentifier().length()).trim());
                }
                break;
            case CLOSE:
                playerMenu.getPlayer().closeInventory();
                break;
            case COINS:
                if ("own".equals(text)) {
                    clanPlayer.setResponse(Response.WithdrawType.OWN);
                } else {
                    clanPlayer.setResponse(Response.WithdrawType.CLAN);
                }
                break;
            case CANCEL:
                if (text.equals("rename")) {
                    plugin.getRenameClan().remove(uuid);
                    break;
                }
                break;
            case SHARE_COINS:
                ClanStats clanStats = clanPlayer.getStats();
                clanStats.setShareCoins(!clanStats.isShareCoins());
                open();
                break;
            case PAGE:
                switch (text) {
                    case "next":
                        if (!((index + 1) >= size)) {
                            page = page + 1;
                            open();
                        }
                        break;
                    case "back":
                        if (page != 0) {
                            page = page - 1;
                            open();
                        }
                        break;
                }
                break;
            case COMMAND:
                if (text.contains("[player]")) {
                    playerMenu.getPlayer().performCommand(text.replace("[player] ", ""));
                    return;
                }
                Utils.dispatchCommand(clanPlayer, text);
                break;
        }
    }
}