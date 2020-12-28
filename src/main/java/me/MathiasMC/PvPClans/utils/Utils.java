package me.MathiasMC.PvPClans.utils;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {

    public static Random getRandom() {
        return PvPClans.getInstance().getRandom();
    }

    public static Pattern getBracket() {
        return PvPClans.getInstance().getBracket();
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static String prefix() {
        return "[" + PvPClans.getInstance().getDescription().getName() + "]";
    }

    public static void info(String text) {
        Bukkit.getLogger().info(prefix() + " " + text);
    }

    public static void warning(String text) {
        Bukkit.getLogger().warning(prefix() + " " + text);
    }

    public static void error(String text) {
        if (!PvPClans.getInstance().getFileUtils().config.getBoolean("debug", false)) return;
        Bukkit.getLogger().severe(prefix() + " " + text);
    }

    public static void exception(StackTraceElement[] stackTraceElement, String text) {
        info("(!) " + prefix() + " has being encountered an error, pasting below for support (!)");
        for (StackTraceElement traceElement : stackTraceElement) {
            error(traceElement.toString());
        }
        info("Message: " + text);
        info(prefix() + " version: " + PvPClans.getInstance().getDescription().getVersion());
        info("Please report this error to me on spigot");
        info("(!) " + prefix() + " (!)");
    }

    public static void sendCommands(final ClanPlayer player, final CommandSender sender, final String path) {
        if (player == null) {
            sendMessageList(sender, "console." + path);
            return;
        }
        dispatchCommandList(player, path);
    }

    public static void sendMessageList(final CommandSender sender, final String path) {
        for (String message : getCommands(path)) {
            sendMessage(sender, message);
        }
    }

    public static void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static List<String> getCommands(final String path) {
        return PvPClans.getInstance().getFileUtils().language.getStringList(path);
    }

    public static void dispatchCommandList(final ClanPlayer clanPlayer, final String path) {
        for (String command : getCommands(path)) {
            dispatchCommand(clanPlayer, command);
        }
    }

    public static void dispatchCommandList(final ClanPlayer player, final ClanPlayer target, final String path) {
        for (String command : getCommands(path)) {
            dispatchCommand(player, target, command);
        }
    }

    public static void dispatchCommand(final ClanPlayer clanPlayer, final String text) {
        dispatch(replacePlaceholders(clanPlayer, color(text)));
    }

    public static void dispatchCommand(final ClanPlayer player, final ClanPlayer target, final String text) {
        dispatch(replacePlaceholders(player, target, color(text)));
    }

    private static void dispatch(String text) {
        PvPClans.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), text);
    }

    public static String replacePlaceholders(final ClanPlayer clanPlayer, String command) {
        command = command.replace("{player}", clanPlayer.getName());
        return replaceInternal(clanPlayer, command);
    }

    public static String replacePlaceholders(final ClanPlayer player, final ClanPlayer target, String command) {
        command = command.replace("{player}", player.getName()).replace("{target}", target.getName());
        return replacePlaceholders(player, command);
    }

    private static String replaceInternal(final ClanPlayer clanPlayer, String command) {
        Clan clan = clanPlayer.getClan();
        if (clan != null) {
            PvPClans plugin = PvPClans.getInstance();
            ClanStats clanStats = clanPlayer.getStats();
            command = command
                    .replace("{clan}", clan.getName())
                    .replace("{id}", String.valueOf(clan.getID()))
                    .replace("{prefix}", ChatColor.translateAlternateColorCodes('&', clan.getPrefix()))
                    .replace("{leader}", plugin.getClanPlayer(clan.getLeader()).getName())
                    .replace("{coins}", String.valueOf(clanStats.getCoins()))
                    .replace("{xp}", String.valueOf(clanStats.getXp()))
                    .replace("{kills}", String.valueOf(clanStats.getKills()))
                    .replace("{deaths}", String.valueOf(clanStats.getDeaths()))
                    .replace("{share}", plugin.getStatsManager().getClanShare(clanPlayer))
                    .replace("{clan_coins}", String.valueOf(clan.getCoins()))
                    .replace("{clan_xp}", String.valueOf(clan.getXp()))
                    .replace("{clan_xp_need}", String.valueOf(plugin.getFileUtils().levels.getLong((clan.getLevel() + 1) + ".xp", 0)))
                    .replace("{clan_kills}", String.valueOf(clan.getKills()))
                    .replace("{clan_deaths}", String.valueOf(clan.getDeaths()))
                    .replace("{clan_level}", String.valueOf(clan.getLevel()))
                    .replace("{clan_members}", String.valueOf(clan.getMembers().size()))
                    .replace("{clan_max_members}", String.valueOf(clan.getMaxMembers()))
                    .replace("{clan_members_limit}", String.valueOf(plugin.getFileUtils().clan.getStringList("member.slot").size()))
                    .replace("{clan_time}", plugin.getStatsManager().getTime(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - clan.getTimeStamp().getTime())))
                    .replace("{online}", plugin.getStatsManager().getOnline(clanPlayer))
                    .replace("{cost_upgrade}", String.valueOf(plugin.getClanManager().getUpgradeCost(clan.getLevel() + 1)))
                    .replace("{cost_upgrade_all}", String.valueOf(plugin.getClanManager().getUpgradeCostAll(clan.getLevel() + 1)))
                    .replace("{cost_rename}", String.valueOf(plugin.getClanManager().getRenameCost(clan.getLevel())))
                    .replace("{cost_rename_all}", String.valueOf(plugin.getClanManager().getRenameCostAll(clan.getLevel())))
                    .replace("{cost_member}", String.valueOf(plugin.getClanManager().getMemberCost(clan.getLevel())))
                    .replace("{cost_member_all}", String.valueOf(plugin.getClanManager().getMemberCostAll(clan.getLevel())))
                    .replace("{rename}", clan.getRename())
            ;

        }
        return color(command);
    }


    public static void alertClan(Clan clan, ClanPlayer player, ClanPlayer target, String path) {
        dispatchCommandList(player, target, path + ".leader");
        for (UUID member : clan.getMembers()) {
            ClanPlayer clanMember = PvPClans.getInstance().getClanPlayer(member);
            if (!clan.isModerator(member)) {
                for (String command : getCommands(path + ".member")) {
                    PvPClans.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), replaceInternal(clanMember, color(command
                            .replace("{player}", player.getName())
                            .replace("{target}", target.getName())
                            .replace("{member}", clanMember.getName()))));
                }
            } else {
                for (String command : getCommands(path + ".moderator")) {
                    PvPClans.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), replaceInternal(clanMember, color(command
                            .replace("{player}", player.getName())
                            .replace("{target}", target.getName())
                            .replace("{member}", clanMember.getName()))));
                }
            }
        }
    }

    public static boolean isValidClan(String text) {
        if (PvPClans.getInstance().getFileUtils().config.getStringList("blacklist").contains(text)) {
            return false;
        }
        if (text.length() > 200) {
            return false;
        }
        return text.matches("^[a-zA-Z0-9]*$");
    }

    public static boolean isValidPerk(String text) {
        if (text.length() > 200) {
            return false;
        }
        return text.matches("^[a-zA-Z0-9()-]*$");
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static double randomDouble(double min, double max) {
        return min + Math.random() * (max - min);
    }
}