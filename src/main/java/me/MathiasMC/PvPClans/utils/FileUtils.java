package me.MathiasMC.PvPClans.utils;

import com.google.common.io.ByteStreams;
import me.MathiasMC.PvPClans.PvPClans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private final PvPClans plugin;

    private final File configFile;
    public FileConfiguration config;

    private final File languageFile;
    public FileConfiguration language;

    private final File levelsFile;
    public FileConfiguration levels;

    private final File clanFile;
    public FileConfiguration clan;

    private final File confirmModeratorFile;
    public FileConfiguration confirmModerator;

    private final File confirmMemberFile;
    public FileConfiguration confirmMember;

    private final File confirmKickFile;
    public FileConfiguration confirmKick;

    private final File marketFile;
    public FileConfiguration market;

    private final File renameFile;
    public FileConfiguration rename;

    private final File confirmRenameFile;
    public FileConfiguration confirmRename;

    private final File memberSizeFile;
    public FileConfiguration memberSize;

    private final File confirmMemberSizeFile;
    public FileConfiguration confirmMemberSize;

    private final File clansFile;
    public FileConfiguration clans;

    private final File upgradeFile;
    public FileConfiguration upgrade;

    private final File confirmUpgradeFile;
    public FileConfiguration confirmUpgrade;

    private final File upgradesFile;
    public FileConfiguration upgrades;

    private final File clanPerksFile;
    public FileConfiguration clanPerks;

    public FileUtils(final PvPClans plugin) {
        this.plugin = plugin;
        final File pluginFolder = getFolder(plugin.getDataFolder().getPath());
        final File guiFolder = getFolder(pluginFolder + File.separator + "gui");
        final File marketFolder = getFolder(guiFolder + File.separator + "market");
        getFolder(pluginFolder + File.separator + "perks");
        final File marketConfirmFolder = getFolder(marketFolder + File.separator + "confirm");
        this.configFile = copyFile(pluginFolder, "config.yml");
        this.languageFile = copyFile(pluginFolder, "language.yml");
        String path;
        if (!plugin.getItemManager().isOld) {
            path = "new";
        } else {
            path = "old";
        }
        this.levelsFile = copyFile(pluginFolder, "levels.yml", path + "/levels.yml");
        this.clanFile = copyFile(guiFolder, "clan.yml", path + "/gui/clan.yml");
        this.confirmModeratorFile = copyFile(guiFolder, "moderator.yml", path + "/gui/moderator.yml");
        this.confirmMemberFile = copyFile(guiFolder, "member.yml", path + "/gui/member.yml");
        this.confirmKickFile = copyFile(guiFolder, "kick.yml", path + "/gui/kick.yml");
        this.upgradesFile = copyFile(guiFolder, "upgrades.yml", path + "/gui/upgrades.yml");
        this.marketFile = copyFile(marketFolder, "market.yml", path + "/gui/market/market.yml");
        this.renameFile = copyFile(marketFolder, "rename.yml", path + "/gui/market/rename.yml");
        this.confirmRenameFile = copyFile(marketConfirmFolder, "rename.yml", path + "/gui/market/confirm/rename.yml");
        this.memberSizeFile = copyFile(marketFolder, "member_size.yml", path + "/gui/market/member_size.yml");
        this.confirmMemberSizeFile = copyFile(marketConfirmFolder, "member_size.yml", path + "/gui/market/confirm/member_size.yml");
        this.upgradeFile = copyFile(marketFolder, "upgrade.yml", path + "/gui/market/upgrade.yml");
        this.confirmUpgradeFile = copyFile(marketConfirmFolder, "upgrade.yml", path + "/gui/market/confirm/upgrade.yml");
        this.clansFile = copyFile(guiFolder, "list.yml", path + "/gui/list.yml");
        this.clanPerksFile = copyFile(guiFolder, "perks.yml", path + "/gui/perks.yml");
        loadConfigs();
    }

    public void loadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        language = YamlConfiguration.loadConfiguration(languageFile);
        levels = YamlConfiguration.loadConfiguration(levelsFile);
        clan = YamlConfiguration.loadConfiguration(clanFile);
        confirmModerator = YamlConfiguration.loadConfiguration(confirmModeratorFile);
        confirmMember = YamlConfiguration.loadConfiguration(confirmMemberFile);
        confirmKick = YamlConfiguration.loadConfiguration(confirmKickFile);
        upgrades = YamlConfiguration.loadConfiguration(upgradesFile);
        market = YamlConfiguration.loadConfiguration(marketFile);
        rename = YamlConfiguration.loadConfiguration(renameFile);
        confirmRename = YamlConfiguration.loadConfiguration(confirmRenameFile);
        memberSize = YamlConfiguration.loadConfiguration(memberSizeFile);
        confirmMemberSize = YamlConfiguration.loadConfiguration(confirmMemberSizeFile);
        upgrade = YamlConfiguration.loadConfiguration(upgradeFile);
        confirmUpgrade = YamlConfiguration.loadConfiguration(confirmUpgradeFile);
        clans = YamlConfiguration.loadConfiguration(clansFile);
        clanPerks = YamlConfiguration.loadConfiguration(clanPerksFile);
    }

    public File getFolder(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public File copyFile(final File folder, final String fileName, final String path) {
        final File file = new File(folder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try {
                    ByteStreams.copy(plugin.getResource(path), new FileOutputStream(file));
                } catch (NullPointerException e) {
                    Utils.info("cant find: " + path);
                }
            } catch (IOException exception) {
                Utils.error("Could not create file " + path);
            }
        }
        return file;
    }

    public File copyFile(final File folder, final String fileName) {
        return copyFile(folder, fileName, fileName);
    }
}
