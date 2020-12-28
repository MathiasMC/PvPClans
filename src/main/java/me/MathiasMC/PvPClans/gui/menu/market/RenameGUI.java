package me.MathiasMC.PvPClans.gui.menu.market;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.managers.ClanManager;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class RenameGUI extends GUI {

    public RenameGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().rename;
    }

    @Override
    public boolean perform(String text) {
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        ClanManager clanManager = plugin.getClanManager();
        long cost = clanManager.getRenameCost(clanPlayer);
        if (!clanManager.canWithdraw(clanPlayer, cost)) {
            Utils.dispatchCommandList(clanPlayer, "gui.rename.enough");
            return false;
        }
        Utils.dispatchCommandList(clanPlayer, "gui.rename.target");
        plugin.getRenameClan().add(uuid);
        return true;
    }
}