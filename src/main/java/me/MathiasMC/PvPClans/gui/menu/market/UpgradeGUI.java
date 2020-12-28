package me.MathiasMC.PvPClans.gui.menu.market;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.gui.menu.market.confirm.ConfirmUpgradeGUI;
import me.MathiasMC.PvPClans.managers.ClanManager;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class UpgradeGUI extends GUI {

    public UpgradeGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().upgrade;
    }

    @Override
    public boolean perform(String text) {
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        long level = clan.getLevel() + 1;
        long xp = plugin.getFileUtils().levels.getLong(level + ".xp", 0);
        if (xp == 0) {
            Utils.dispatchCommandList(clanPlayer, "gui.upgrade.max");
            return false;
        }
        if (clan.getXp() < xp) {
            Utils.dispatchCommandList(clanPlayer, "gui.upgrade.enough-xp");
            return false;
        }
        ClanManager clanManager = plugin.getClanManager();
        long cost = clanManager.getUpgradeCost(clanPlayer);
        if (!clanManager.canWithdraw(clanPlayer, cost)) {
            Utils.dispatchCommandList(clanPlayer, "gui.upgrade.enough-coins");
            return false;
        }
        new ConfirmUpgradeGUI(playerMenu).open();
        return true;
    }
}