package me.MathiasMC.PvPClans.gui.menu.market.confirm;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.api.events.ClanUpgradeEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public class ConfirmUpgradeGUI extends GUI {

    public ConfirmUpgradeGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().confirmUpgrade;
    }

    @Override
    public boolean perform(String text) {
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        long level = clan.getLevel() + 1;
        Response.Withdraw withdraw = plugin.getClanManager().withdraw(clanPlayer, plugin.getClanManager().getUpgradeCost(clanPlayer));
        switch (withdraw) {
            case ENOUGH:
                Utils.dispatchCommandList(clanPlayer, "gui.upgrade.enough-coins");
                break;
            case CANCELLED:
                return false;
        }
        ClanUpgradeEvent clanUpgradeEvent = new ClanUpgradeEvent(clan, level);
        plugin.getServer().getPluginManager().callEvent(clanUpgradeEvent);
        if (clanUpgradeEvent.isCancelled()) {
            return false;
        }
        clanUpgradeEvent.execute();
        clan.saveAsync();
        super.open();
        if (plugin.getFileUtils().levels.contains(level + ".leader")) {
            for (String command : plugin.getFileUtils().levels.getStringList(level + ".leader")) {
                Utils.dispatchCommand(clanPlayer, command);
            }
        }
        List<UUID> members = clan.getMembers();
        List<UUID> moderators = clan.getModerators();
        members.removeAll(moderators);
        if (plugin.getFileUtils().levels.contains(level + ".member")) {
            for (UUID member : members) {
                for (String command : plugin.getFileUtils().levels.getStringList(level + ".member")) {
                    Utils.dispatchCommand(plugin.getClanPlayer(member), command);
                }
            }
        }
        if (plugin.getFileUtils().levels.contains(level + ".moderator")) {
            for (UUID moderator : moderators) {
                for (String command : plugin.getFileUtils().levels.getStringList(level + ".moderator")) {
                    Utils.dispatchCommand(plugin.getClanPlayer(moderator), command);
                }
            }
        }
        return true;
    }
}