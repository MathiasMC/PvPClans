package me.MathiasMC.PvPClans.api;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Perk {

    public abstract String getIdentifier();

    public abstract ItemStack getItemStack();

    public abstract String getName();

    public abstract List<String> getLore();

    public List<String> getLeader() { return null; }

    public List<String> getModerator() { return null; }

    public List<String> getMember() { return null; }

    public List<String> getLeaderRemove() { return null; }

    public List<String> getModeratorRemove() { return null; }

    public List<String> getMemberRemove() { return null; }

    public void setupConfig() {

    }

    private final File file;

    private final FileConfiguration config;

    public abstract String onRequest(ClanPlayer clanPlayer, String placeholder);

    public Perk() {
        this.file = new File(PvPClans.getInstance().getDataFolder() + File.separator + "perks", getIdentifier() + ".yml");
        boolean created = false;
        if (!file.exists()) {
            try {
                created = file.createNewFile();
            } catch (Exception ignored) {}
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (!created) return;
        config.set("material", getItemStack());
        config.set("name", getName());
        config.set("lore", getLore());
        if (getLeader() != null) config.set("leader", getLeader());
        if (getModerator() != null) config.set("moderator", getModerator());
        if (getMember() != null) config.set("member", getMember());
        if (getLeaderRemove() != null) config.set("leader-remove", getLeaderRemove());
        if (getModeratorRemove() != null) config.set("moderator-remove", getModeratorRemove());
        if (getMemberRemove() != null) config.set("member-remove", getMemberRemove());
        setupConfig();
        saveConfig();
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public boolean saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean register() {
        if (PvPClans.getInstance().getPerks().containsKey(getIdentifier())) {
            return false;
        }
        PvPClans.getInstance().getPerks().put(getIdentifier(), this);
        return true;
    }
}
