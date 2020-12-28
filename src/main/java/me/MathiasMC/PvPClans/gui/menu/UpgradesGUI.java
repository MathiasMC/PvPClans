package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpgradesGUI extends GUI {

    public UpgradesGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().upgrades;
    }

    @Override
    public void setItems() {
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        FileConfiguration levels = plugin.getFileUtils().levels;
        List<Integer> slots = file.getIntegerList("list");
        int showMax = file.getInt("show.amount");
        int show = 0;
        long level = clan.getLevel() + 1;
        for (int i : slots) {
            ItemStack itemStack;
            ItemMeta itemMeta;
            if (levels.contains(level + ".xp")) {
                String xpNeed = String.valueOf(plugin.getFileUtils().levels.getLong(level + ".xp", 0));
                if (show >= showMax) {
                    itemStack = plugin.getItemManager().getItemStack(file.getString("show.material"));
                    itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(Utils.replacePlaceholders(clanPlayer, file.getString("show.name").replace("{clan_xp_need}", xpNeed)));
                    final ArrayList<String> list = new ArrayList<>();
                    for (String lores : file.getStringList("show.lore")) {
                        list.add(Utils.replacePlaceholders(clanPlayer, lores.replace("{clan_xp_need}", xpNeed)));
                    }
                    itemMeta.setLore(list);
                } else {
                    itemStack = plugin.getItemManager().getItemStack(levels.getString(level + ".material"));
                    itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(Utils.replacePlaceholders(clanPlayer, levels.getString(level + ".name").replace("{clan_xp_need}", xpNeed)));
                    final ArrayList<String> list = new ArrayList<>();
                    for (String lores : levels.getStringList(level + ".lore")) {
                        list.add(Utils.replacePlaceholders(clanPlayer, lores.replace("{clan_xp_need}", xpNeed)));
                    }
                    itemMeta.setLore(list);
                }
            } else {
                itemStack = plugin.getItemManager().getItemStack(file.getString("material"));
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(Utils.replacePlaceholders(clanPlayer, file.getString("name")));
                final ArrayList<String> list = new ArrayList<>();
                for (String lores : file.getStringList("lore")) {
                    list.add(Utils.replacePlaceholders(clanPlayer, lores));
                }
                itemMeta.setLore(list);
            }
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            level++;
            show++;
            inventory.setItem(i, itemStack);
        }
    }
}
