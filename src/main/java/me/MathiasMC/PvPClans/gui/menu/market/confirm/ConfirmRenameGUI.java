package me.MathiasMC.PvPClans.gui.menu.market.confirm;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.api.events.ClanRenameEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class ConfirmRenameGUI extends GUI {

    public ConfirmRenameGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().confirmRename;
    }

    @Override
    public boolean perform(String text) {
            UUID uuid = playerMenu.getUniqueId();
            ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
            Clan clan = clanPlayer.getClan();
            if (!plugin.getRenameClan().contains(uuid)) return false;
            Response.Withdraw withdraw = plugin.getClanManager().withdraw(clanPlayer, plugin.getClanManager().getRenameCost(clanPlayer));
            switch (withdraw) {
                case ENOUGH:
                    Utils.dispatchCommandList(clanPlayer, "gui.rename.enough");
                    break;
                case CANCELLED:
                    return false;
            }
            ClanRenameEvent clanRenameEvent = new ClanRenameEvent(clan, playerMenu.getRename());
            plugin.getServer().getPluginManager().callEvent(clanRenameEvent);
            if (clanRenameEvent.isCancelled()) {
                return false;
            }
            switch (clanRenameEvent.getResponse()) {
                case EXISTS:
                    for (String command : Utils.getCommands("gui.rename.exists")) {
                        Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanRenameEvent.getName()));
                    }
                    return false;
                case NAME:
                    for (String command : Utils.getCommands("gui.rename.name")) {
                        Utils.dispatchCommand(clanPlayer, command.replace("{clan}", clanRenameEvent.getName()));
                    }
                    return false;
                case SUCCESS:
                    clanRenameEvent.execute();
                    clan.saveAsync();
                    Utils.alertClan(clan, clanPlayer, clanPlayer, "gui.rename");
                    return true;
            }
            return false;
    }
}