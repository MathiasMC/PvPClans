package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

public class PerksGUI extends GUI {

    public PerksGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().clanPerks;
    }

    ArrayList<String> list;

    @Override
    public void setItems() {
        list = new ArrayList<>();
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        list.addAll(clan.getPerks().keySet());
        size = list.size();
        List<Integer> slots = file.getIntegerList("list");
        int perPage = slots.size();
        for (int i = 0; i < perPage; i++) {
            index = perPage * page + i;
            if (index >= list.size()) break;
            if (list.get(index) != null) {
                String type = list.get(index);
                if (plugin.getPerks().containsKey(type)) {
                    Perk perk = plugin.getPerks().get(type);
                    ItemStack itemStack = perk.getConfig().getItemStack("material");
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(Utils.color(setPlaceholders(clanPlayer, perk.getIdentifier(), perk.getConfig().getString("name"))));
                    final ArrayList<String> list = new ArrayList<>();
                    for (String lores : perk.getConfig().getStringList("lore")) {
                        list.add(Utils.replacePlaceholders(clanPlayer, Utils.color(setPlaceholders(clanPlayer, perk.getIdentifier(), lores))));
                    }
                    itemMeta.setLore(list);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemStack.setItemMeta(itemMeta);
                    inventory.setItem(slots.get(i), itemStack);
                    playerMenu.getPerks().put(slots.get(i), perk.getIdentifier());
                }
            }
        }
    }

    private String setPlaceholders(ClanPlayer clanPlayer, String identifier, String text) {
        Matcher matcher = Utils.getBracket().matcher(text);
        while (matcher.find()) {
            String get = PvPClans.getInstance().getPerks().get(identifier).onRequest(clanPlayer, matcher.group(1));
            if (get != null) {
                text = text.replace("{" + matcher.group(1) + "}", get);
            }
            text = text.replace("{enabled}", getEnable(identifier)).replace("{value}", getValue(identifier));
        }
        return text;
    }

    private String getEnable(String identifier) {
        if (clanPlayer.getClan().isPerkActive(identifier)) {
            return ChatColor.translateAlternateColorCodes('&', PvPClans.getInstance().getFileUtils().language.getString("translate.perk.enabled"));
        }
        return ChatColor.translateAlternateColorCodes('&', PvPClans.getInstance().getFileUtils().language.getString("translate.perk.disabled"));
    }

    private String getValue(String identifier) {
        int[] array = clanPlayer.getClan().getPerk(identifier);
        return String.valueOf(array[0]);
    }
}