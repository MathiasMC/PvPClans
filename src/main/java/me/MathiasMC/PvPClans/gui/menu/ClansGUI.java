package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClansGUI extends GUI {

    protected int page = 0;

    protected int index = 0;

    public ClansGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().clans;
    }

    ArrayList<Long> list;

    @Override
    public void setItems() {
        List<Integer> slots = file.getIntegerList("list");
        int perPage = slots.size();
        list = new ArrayList<>();
        for (Map.Entry<Long, Clan> entry : plugin.getClans().entrySet()) {
            list.add(entry.getValue().getID());
        }
        size = list.size();
        for (int i = 0; i < perPage; i++) {
            index = perPage * page + i;
            if (index >= list.size()) break;
            if (list.get(index) != null) {
                plugin.getItemManager().addHead(inventory, slots.get(i), plugin.getClanPlayer(plugin.getClan(list.get(index)).getLeader()), file.getString("name"), file.getStringList("lore"));
            }
        }
    }
}