package me.MathiasMC.PvPClans.gui.menu.market;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import org.bukkit.configuration.file.FileConfiguration;

public class MarketGUI extends GUI {

    public MarketGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().market;
    }
}