package me.MathiasMC.PvPClans.managers;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatsManager {

    private final PvPClans plugin;

    public StatsManager(final PvPClans plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateTopMap,0, plugin.getFileUtils().config.getLong("top.update") * 20);
    }
    private LinkedHashMap<Long, Long> topKills = new LinkedHashMap<>();
    private LinkedHashMap<Long, Long> topDeaths = new LinkedHashMap<>();
    private LinkedHashMap<Long, Long> topXp = new LinkedHashMap<>();
    private LinkedHashMap<Long, Long> topLevel = new LinkedHashMap<>();
    private LinkedHashMap<Long, Long> topCoins = new LinkedHashMap<>();

    public String getClanShare(final ClanPlayer clanPlayer) {
        Clan clan = clanPlayer.getClan();
        if (clan == null) return "";
        if (clanPlayer.getStats().isShareCoins()) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().language.getString("translate.share.enabled"));
        }
        return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().language.getString("translate.share.disabled"));
    }

    public String getOnline(ClanPlayer clanPlayer) {
        if (plugin.getServer().getOfflinePlayer(clanPlayer.getUniqueId()).isOnline()) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().language.getString("translate.online.enabled"));
        }
        return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().language.getString("translate.online.disabled"));
    }

    public boolean canProgress(final Player player) {
        if (!plugin.getFileUtils().config.contains("worlds")) return true;
        return !plugin.getFileUtils().config.getStringList("worlds").contains(player.getWorld().getName());
    }

    public String getTopKills(final int number, final boolean isName) {
        return getTop(number - 1, isName, topKills);
    }

    public String getTopDeaths(final int number, final boolean isName) {
        return getTop(number - 1, isName, topDeaths);
    }

    public String getTopXp(final int number, final boolean isName) {
        return getTop(number - 1, isName, topXp);
    }

    public String getTopLevel(final int number, final boolean isName) {
        return getTop(number - 1, isName, topLevel);
    }

    public String getTopCoins(final int number, final boolean isName) {
        return getTop(number - 1, isName, topCoins);
    }

    private String getTop(final int number, final boolean key, final LinkedHashMap<Long, Long> topMap) {
        if (key) {
            final ArrayList<Long> map = new ArrayList<>(topMap.keySet());
            if (map.size() > number && plugin.getClans().containsKey(map.get(number))) {
                return plugin.getClan(map.get(number)).getName();
            } else {
                return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().config.getString("top.name"));
            }
        } else {
            final ArrayList<Long> map = new ArrayList<>(topMap.values());
            if (map.size() > number) {
                return String.valueOf(map.get(number));
            } else {
                return ChatColor.translateAlternateColorCodes('&', plugin.getFileUtils().config.getString("top.value"));
            }
        }
    }

    public void updateTopMap() {
        Map<Long, Long> unsortedKills = new HashMap<>();
        Map<Long, Long> unsortedDeaths = new HashMap<>();
        Map<Long, Long> unsortedXp = new HashMap<>();
        Map<Long, Long> unsortedLevel = new HashMap<>();
        Map<Long, Long> unsortedCoins = new HashMap<>();
        for (long clanID : plugin.getClans().keySet()) {
            Clan clan = plugin.getClan(clanID);
            unsortedKills.put(clanID, clan.getKills());
            unsortedDeaths.put(clanID, clan.getDeaths());
            unsortedXp.put(clanID, clan.getXp());
            unsortedLevel.put(clanID, clan.getLevel());
            unsortedCoins.put(clanID, clan.getCoins());
        }
        topKills = getSortedMap(unsortedKills);
        topDeaths = getSortedMap(unsortedDeaths);
        topXp = getSortedMap(unsortedXp);
        topLevel = getSortedMap(unsortedLevel);
        topCoins = getSortedMap(unsortedCoins);
    }

    private LinkedHashMap<Long, Long> getSortedMap(final Map<Long, Long> map) {
        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public String getTime(long seconds){
        long millis = seconds * 1000;
        String time = "";
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long second = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (days > 1) {
            time += days + " " + plugin.getFileUtils().language.getString("translate.time.days") + " ";
        } else if (days == 1) {
            time += days + " " + plugin.getFileUtils().language.getString("translate.time.day") + " ";
        }
        if (hours > 1) {
            time += hours + " " + plugin.getFileUtils().language.getString("translate.time.hours") + " ";
        } else if (hours == 1) {
            time += hours + " " + plugin.getFileUtils().language.getString("translate.time.hour") + " ";
        }
        if (minutes > 1) {
            time += minutes + " " + plugin.getFileUtils().language.getString("translate.time.minutes") + " ";
        } else if (minutes == 1) {
            time += minutes + " " + plugin.getFileUtils().language.getString("translate.time.minute") + " ";
        }
        if (second > 1) {
            time += second + " " + plugin.getFileUtils().language.getString("translate.time.seconds");
        } else if (second == 1) {
            time += second + " " + plugin.getFileUtils().language.getString("translate.time.second");
        }
        if (time.endsWith(" ")) {
            return time.replaceAll("\\s+$", "");
        }
        return time;
    }
}