package me.MathiasMC.PvPClans.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    private final PvPClans plugin;

    private final ItemStack playerHead;

    public final boolean isOld;

    public ItemManager(final PvPClans plugin) {
        this.plugin = plugin;
        if (isOld()) {
            isOld = true;
            playerHead = getItemStack("397:3");
        } else {
            isOld = false;
            playerHead = getItemStack("PLAYER_HEAD");
        }
    }

    private boolean isOld() {
        if (plugin.getServer().getVersion().contains("1.8")) return true;
        if (plugin.getServer().getVersion().contains("1.9")) return true;
        if (plugin.getServer().getVersion().contains("1.10")) return true;
        if (plugin.getServer().getVersion().contains("1.11")) return true;
        return plugin.getServer().getVersion().contains("1.12");
    }

    public ItemStack getItemStack(String material) {
        try {
            if (!isOld) {
                return new ItemStack(Material.getMaterial(material.toUpperCase()), 1);
            }
            String[] parts = material.split(":");
            int id = Integer.parseInt(parts[0]);
            if (parts.length == 2) {
                short data = Short.parseShort(parts[1]);
                return new ItemStack(Material.getMaterial(id), 1, data);
            }
            return new ItemStack(Material.getMaterial(id), 1);
        } catch (Exception e) {
            return new ItemStack(Material.DIRT);
        }
    }

    public ItemStack getHeadAsync(final UUID uuid) {
        if (plugin.getPlayerHeads().containsKey(uuid)) {
            return plugin.getPlayerHeads().get(uuid).clone();
        }
        final ItemStack itemStack = playerHead.clone();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        setTexture(itemStack, skullMeta, uuid);
        plugin.getPlayerHeads().put(uuid, itemStack);
        return itemStack;
    }

    private void setTexture(ItemStack item, SkullMeta meta, UUID uuid) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String texture;
            String signature;
            URL url;
            try {
                url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            } catch (MalformedURLException ignored) {
                return;
            }
            try {
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                texture = json.get("value").getAsString();
                signature = json.get("signature").getAsString();
            } catch (IOException e) {
                return;
            }
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture, signature));
            Field field;
            try {
                field = meta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(meta, profile);
                item.setItemMeta(meta);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        });
    }

    public void addHead(Inventory inventory, int slot, ClanPlayer clanPlayer, String name, List<String> lore) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            ItemStack itemStack = getHeadAsync(clanPlayer.getUniqueId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Utils.replacePlaceholders(clanPlayer, name));
            ArrayList<String> list = new ArrayList<>();
            for (String lores : lore) {
                list.add(Utils.replacePlaceholders(clanPlayer, lores));
            }
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
        });
    }

    public void setItems(ClanPlayer clanPlayer, Inventory inventory, FileConfiguration file) {
        for (String item : file.getConfigurationSection("items").getKeys(false)) {
            ItemStack itemStack = getItemStack(file.getString("items." + item + ".material"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Utils.replacePlaceholders(clanPlayer, file.getString("items." + item + ".name")));
            ArrayList<String> list = new ArrayList<>();
            for (String lores : file.getStringList("items." + item + ".lore")) {
                list.add(Utils.replacePlaceholders(clanPlayer, lores));
            }
            itemMeta.setLore(list);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(item), itemStack);
        }
    }
}
