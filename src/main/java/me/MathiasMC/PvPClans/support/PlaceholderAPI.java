package me.MathiasMC.PvPClans.support;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final PvPClans plugin;

    public PlaceholderAPI(PvPClans plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "pvpclans";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(player == null){
            return "";
        }
        if (!plugin.getStatsManager().canProgress(player)) {
            return null;
        }
        if(identifier.equals("top_kills_name_1")) {
            return plugin.getStatsManager().getTopKills( 1, true);
        }
        if(identifier.equals("top_kills_value_1")) {
            return plugin.getStatsManager().getTopKills( 1, false);
        }
        if(identifier.equals("top_kills_name_2")) {
            return plugin.getStatsManager().getTopKills( 2, true);
        }
        if(identifier.equals("top_kills_value_2")) {
            return plugin.getStatsManager().getTopKills( 2, false);
        }
        if(identifier.equals("top_kills_name_3")) {
            return plugin.getStatsManager().getTopKills( 3, true);
        }
        if(identifier.equals("top_kills_value_3")) {
            return plugin.getStatsManager().getTopKills( 3, false);
        }
        if(identifier.equals("top_kills_name_4")) {
            return plugin.getStatsManager().getTopKills( 4, true);
        }
        if(identifier.equals("top_kills_value_4")) {
            return plugin.getStatsManager().getTopKills( 4, false);
        }
        if(identifier.equals("top_kills_name_5")) {
            return plugin.getStatsManager().getTopKills( 5, true);
        }
        if(identifier.equals("top_kills_value_5")) {
            return plugin.getStatsManager().getTopKills( 5, false);
        }
        if(identifier.equals("top_kills_name_6")) {
            return plugin.getStatsManager().getTopKills( 6, true);
        }
        if(identifier.equals("top_kills_value_6")) {
            return plugin.getStatsManager().getTopKills( 6, false);
        }
        if(identifier.equals("top_kills_name_7")) {
            return plugin.getStatsManager().getTopKills( 7, true);
        }
        if(identifier.equals("top_kills_value_7")) {
            return plugin.getStatsManager().getTopKills( 7, false);
        }
        if(identifier.equals("top_kills_name_8")) {
            return plugin.getStatsManager().getTopKills( 8, true);
        }
        if(identifier.equals("top_kills_value_8")) {
            return plugin.getStatsManager().getTopKills( 8, false);
        }
        if(identifier.equals("top_kills_name_9")) {
            return plugin.getStatsManager().getTopKills( 9, true);
        }
        if(identifier.equals("top_kills_value_9")) {
            return plugin.getStatsManager().getTopKills( 9, false);
        }
        if(identifier.equals("top_kills_name_10")) {
            return plugin.getStatsManager().getTopKills( 10, true);
        }
        if(identifier.equals("top_kills_value_10")) {
            return plugin.getStatsManager().getTopKills( 10, false);
        }
        if(identifier.equals("top_kills_name_11")) {
            return plugin.getStatsManager().getTopKills( 11, true);
        }
        if(identifier.equals("top_kills_value_11")) {
            return plugin.getStatsManager().getTopKills( 11, false);
        }
        if(identifier.equals("top_kills_name_12")) {
            return plugin.getStatsManager().getTopKills( 12, true);
        }
        if(identifier.equals("top_kills_value_12")) {
            return plugin.getStatsManager().getTopKills( 12, false);
        }
        if(identifier.equals("top_kills_name_13")) {
            return plugin.getStatsManager().getTopKills( 13, true);
        }
        if(identifier.equals("top_kills_value_13")) {
            return plugin.getStatsManager().getTopKills( 13, false);
        }
        if(identifier.equals("top_kills_name_14")) {
            return plugin.getStatsManager().getTopKills( 14, true);
        }
        if(identifier.equals("top_kills_value_14")) {
            return plugin.getStatsManager().getTopKills( 14, false);
        }
        if(identifier.equals("top_kills_name_15")) {
            return plugin.getStatsManager().getTopKills( 15, true);
        }
        if(identifier.equals("top_kills_value_15")) {
            return plugin.getStatsManager().getTopKills( 15, false);
        }
        if(identifier.equals("top_deaths_name_1")) {
            return plugin.getStatsManager().getTopDeaths( 1, true);
        }
        if(identifier.equals("top_deaths_value_1")) {
            return plugin.getStatsManager().getTopDeaths( 1, false);
        }
        if(identifier.equals("top_deaths_name_2")) {
            return plugin.getStatsManager().getTopDeaths( 2, true);
        }
        if(identifier.equals("top_deaths_value_2")) {
            return plugin.getStatsManager().getTopDeaths( 2, false);
        }
        if(identifier.equals("top_deaths_name_3")) {
            return plugin.getStatsManager().getTopDeaths( 3, true);
        }
        if(identifier.equals("top_deaths_value_3")) {
            return plugin.getStatsManager().getTopDeaths( 3, false);
        }
        if(identifier.equals("top_deaths_name_4")) {
            return plugin.getStatsManager().getTopDeaths( 4, true);
        }
        if(identifier.equals("top_deaths_value_4")) {
            return plugin.getStatsManager().getTopDeaths( 4, false);
        }
        if(identifier.equals("top_deaths_name_5")) {
            return plugin.getStatsManager().getTopDeaths( 5, true);
        }
        if(identifier.equals("top_deaths_value_5")) {
            return plugin.getStatsManager().getTopDeaths( 5, false);
        }
        if(identifier.equals("top_deaths_name_6")) {
            return plugin.getStatsManager().getTopDeaths( 6, true);
        }
        if(identifier.equals("top_deaths_value_6")) {
            return plugin.getStatsManager().getTopDeaths( 6, false);
        }
        if(identifier.equals("top_deaths_name_7")) {
            return plugin.getStatsManager().getTopDeaths( 7, true);
        }
        if(identifier.equals("top_deaths_value_7")) {
            return plugin.getStatsManager().getTopDeaths( 7, false);
        }
        if(identifier.equals("top_deaths_name_8")) {
            return plugin.getStatsManager().getTopDeaths( 8, true);
        }
        if(identifier.equals("top_deaths_value_8")) {
            return plugin.getStatsManager().getTopDeaths( 8, false);
        }
        if(identifier.equals("top_deaths_name_9")) {
            return plugin.getStatsManager().getTopDeaths( 9, true);
        }
        if(identifier.equals("top_deaths_value_9")) {
            return plugin.getStatsManager().getTopDeaths( 9, false);
        }
        if(identifier.equals("top_deaths_name_10")) {
            return plugin.getStatsManager().getTopDeaths( 10, true);
        }
        if(identifier.equals("top_deaths_value_10")) {
            return plugin.getStatsManager().getTopDeaths( 10, false);
        }
        if(identifier.equals("top_deaths_name_11")) {
            return plugin.getStatsManager().getTopDeaths( 11, true);
        }
        if(identifier.equals("top_deaths_value_11")) {
            return plugin.getStatsManager().getTopDeaths( 11, false);
        }
        if(identifier.equals("top_deaths_name_12")) {
            return plugin.getStatsManager().getTopDeaths( 12, true);
        }
        if(identifier.equals("top_deaths_value_12")) {
            return plugin.getStatsManager().getTopDeaths( 12, false);
        }
        if(identifier.equals("top_deaths_name_13")) {
            return plugin.getStatsManager().getTopDeaths( 13, true);
        }
        if(identifier.equals("top_deaths_value_13")) {
            return plugin.getStatsManager().getTopDeaths( 13, false);
        }
        if(identifier.equals("top_deaths_name_14")) {
            return plugin.getStatsManager().getTopDeaths( 14, true);
        }
        if(identifier.equals("top_deaths_value_14")) {
            return plugin.getStatsManager().getTopDeaths( 14, false);
        }
        if(identifier.equals("top_deaths_name_15")) {
            return plugin.getStatsManager().getTopDeaths( 15, true);
        }
        if(identifier.equals("top_deaths_value_15")) {
            return plugin.getStatsManager().getTopDeaths( 15, false);
        }
        if(identifier.equals("top_xp_name_1")) {
            return plugin.getStatsManager().getTopXp( 1, true);
        }
        if(identifier.equals("top_xp_value_1")) {
            return plugin.getStatsManager().getTopXp( 1, false);
        }
        if(identifier.equals("top_xp_name_2")) {
            return plugin.getStatsManager().getTopXp( 2, true);
        }
        if(identifier.equals("top_xp_value_2")) {
            return plugin.getStatsManager().getTopXp( 2, false);
        }
        if(identifier.equals("top_xp_name_3")) {
            return plugin.getStatsManager().getTopXp( 3, true);
        }
        if(identifier.equals("top_xp_value_3")) {
            return plugin.getStatsManager().getTopXp( 3, false);
        }
        if(identifier.equals("top_xp_name_4")) {
            return plugin.getStatsManager().getTopXp( 4, true);
        }
        if(identifier.equals("top_xp_value_4")) {
            return plugin.getStatsManager().getTopXp( 4, false);
        }
        if(identifier.equals("top_xp_name_5")) {
            return plugin.getStatsManager().getTopXp( 5, true);
        }
        if(identifier.equals("top_xp_value_5")) {
            return plugin.getStatsManager().getTopXp( 5, false);
        }
        if(identifier.equals("top_xp_name_6")) {
            return plugin.getStatsManager().getTopXp( 6, true);
        }
        if(identifier.equals("top_xp_value_6")) {
            return plugin.getStatsManager().getTopXp( 6, false);
        }
        if(identifier.equals("top_xp_name_7")) {
            return plugin.getStatsManager().getTopXp( 7, true);
        }
        if(identifier.equals("top_xp_value_7")) {
            return plugin.getStatsManager().getTopXp( 7, false);
        }
        if(identifier.equals("top_xp_name_8")) {
            return plugin.getStatsManager().getTopXp( 8, true);
        }
        if(identifier.equals("top_xp_value_8")) {
            return plugin.getStatsManager().getTopXp( 8, false);
        }
        if(identifier.equals("top_xp_name_9")) {
            return plugin.getStatsManager().getTopXp( 9, true);
        }
        if(identifier.equals("top_xp_value_9")) {
            return plugin.getStatsManager().getTopXp( 9, false);
        }
        if(identifier.equals("top_xp_name_10")) {
            return plugin.getStatsManager().getTopXp( 10, true);
        }
        if(identifier.equals("top_xp_value_10")) {
            return plugin.getStatsManager().getTopXp( 10, false);
        }
        if(identifier.equals("top_xp_name_11")) {
            return plugin.getStatsManager().getTopXp( 11, true);
        }
        if(identifier.equals("top_xp_value_11")) {
            return plugin.getStatsManager().getTopXp( 11, false);
        }
        if(identifier.equals("top_xp_name_12")) {
            return plugin.getStatsManager().getTopXp( 12, true);
        }
        if(identifier.equals("top_xp_value_12")) {
            return plugin.getStatsManager().getTopXp( 12, false);
        }
        if(identifier.equals("top_xp_name_13")) {
            return plugin.getStatsManager().getTopXp( 13, true);
        }
        if(identifier.equals("top_xp_value_13")) {
            return plugin.getStatsManager().getTopXp( 13, false);
        }
        if(identifier.equals("top_xp_name_14")) {
            return plugin.getStatsManager().getTopXp( 14, true);
        }
        if(identifier.equals("top_xp_value_14")) {
            return plugin.getStatsManager().getTopXp( 14, false);
        }
        if(identifier.equals("top_xp_name_15")) {
            return plugin.getStatsManager().getTopXp( 15, true);
        }
        if(identifier.equals("top_xp_value_15")) {
            return plugin.getStatsManager().getTopXp( 15, false);
        }
        if(identifier.equals("top_level_name_1")) {
            return plugin.getStatsManager().getTopLevel( 1, true);
        }
        if(identifier.equals("top_level_value_1")) {
            return plugin.getStatsManager().getTopLevel( 1, false);
        }
        if(identifier.equals("top_level_name_2")) {
            return plugin.getStatsManager().getTopLevel( 2, true);
        }
        if(identifier.equals("top_level_value_2")) {
            return plugin.getStatsManager().getTopLevel( 2, false);
        }
        if(identifier.equals("top_level_name_3")) {
            return plugin.getStatsManager().getTopLevel( 3, true);
        }
        if(identifier.equals("top_level_value_3")) {
            return plugin.getStatsManager().getTopLevel( 3, false);
        }
        if(identifier.equals("top_level_name_4")) {
            return plugin.getStatsManager().getTopLevel( 4, true);
        }
        if(identifier.equals("top_level_value_4")) {
            return plugin.getStatsManager().getTopLevel( 4, false);
        }
        if(identifier.equals("top_level_name_5")) {
            return plugin.getStatsManager().getTopLevel( 5, true);
        }
        if(identifier.equals("top_level_value_5")) {
            return plugin.getStatsManager().getTopLevel( 5, false);
        }
        if(identifier.equals("top_level_name_6")) {
            return plugin.getStatsManager().getTopLevel( 6, true);
        }
        if(identifier.equals("top_level_value_6")) {
            return plugin.getStatsManager().getTopLevel( 6, false);
        }
        if(identifier.equals("top_level_name_7")) {
            return plugin.getStatsManager().getTopLevel( 7, true);
        }
        if(identifier.equals("top_level_value_7")) {
            return plugin.getStatsManager().getTopLevel( 7, false);
        }
        if(identifier.equals("top_level_name_8")) {
            return plugin.getStatsManager().getTopLevel( 8, true);
        }
        if(identifier.equals("top_level_value_8")) {
            return plugin.getStatsManager().getTopLevel( 8, false);
        }
        if(identifier.equals("top_level_name_9")) {
            return plugin.getStatsManager().getTopLevel( 9, true);
        }
        if(identifier.equals("top_level_value_9")) {
            return plugin.getStatsManager().getTopLevel( 9, false);
        }
        if(identifier.equals("top_level_name_10")) {
            return plugin.getStatsManager().getTopLevel( 10, true);
        }
        if(identifier.equals("top_level_value_10")) {
            return plugin.getStatsManager().getTopLevel( 10, false);
        }
        if(identifier.equals("top_level_name_11")) {
            return plugin.getStatsManager().getTopLevel( 11, true);
        }
        if(identifier.equals("top_level_value_11")) {
            return plugin.getStatsManager().getTopLevel( 11, false);
        }
        if(identifier.equals("top_level_name_12")) {
            return plugin.getStatsManager().getTopLevel( 12, true);
        }
        if(identifier.equals("top_level_value_12")) {
            return plugin.getStatsManager().getTopLevel( 12, false);
        }
        if(identifier.equals("top_level_name_13")) {
            return plugin.getStatsManager().getTopLevel( 13, true);
        }
        if(identifier.equals("top_level_value_13")) {
            return plugin.getStatsManager().getTopLevel( 13, false);
        }
        if(identifier.equals("top_level_name_14")) {
            return plugin.getStatsManager().getTopLevel( 14, true);
        }
        if(identifier.equals("top_level_value_14")) {
            return plugin.getStatsManager().getTopLevel( 14, false);
        }
        if(identifier.equals("top_level_name_15")) {
            return plugin.getStatsManager().getTopLevel( 15, true);
        }
        if(identifier.equals("top_level_value_15")) {
            return plugin.getStatsManager().getTopLevel( 15, false);
        }
        if(identifier.equals("top_coins_name_1")) {
            return plugin.getStatsManager().getTopCoins( 1, true);
        }
        if(identifier.equals("top_coins_value_1")) {
            return plugin.getStatsManager().getTopCoins( 1, false);
        }
        if(identifier.equals("top_coins_name_2")) {
            return plugin.getStatsManager().getTopCoins( 2, true);
        }
        if(identifier.equals("top_coins_value_2")) {
            return plugin.getStatsManager().getTopCoins( 2, false);
        }
        if(identifier.equals("top_coins_name_3")) {
            return plugin.getStatsManager().getTopCoins( 3, true);
        }
        if(identifier.equals("top_coins_value_3")) {
            return plugin.getStatsManager().getTopCoins( 3, false);
        }
        if(identifier.equals("top_coins_name_4")) {
            return plugin.getStatsManager().getTopCoins( 4, true);
        }
        if(identifier.equals("top_coins_value_4")) {
            return plugin.getStatsManager().getTopCoins( 4, false);
        }
        if(identifier.equals("top_coins_name_5")) {
            return plugin.getStatsManager().getTopCoins( 5, true);
        }
        if(identifier.equals("top_coins_value_5")) {
            return plugin.getStatsManager().getTopCoins( 5, false);
        }
        if(identifier.equals("top_coins_name_6")) {
            return plugin.getStatsManager().getTopCoins( 6, true);
        }
        if(identifier.equals("top_coins_value_6")) {
            return plugin.getStatsManager().getTopCoins( 6, false);
        }
        if(identifier.equals("top_coins_name_7")) {
            return plugin.getStatsManager().getTopCoins( 7, true);
        }
        if(identifier.equals("top_coins_value_7")) {
            return plugin.getStatsManager().getTopCoins( 7, false);
        }
        if(identifier.equals("top_coins_name_8")) {
            return plugin.getStatsManager().getTopCoins( 8, true);
        }
        if(identifier.equals("top_coins_value_8")) {
            return plugin.getStatsManager().getTopCoins( 8, false);
        }
        if(identifier.equals("top_coins_name_9")) {
            return plugin.getStatsManager().getTopCoins( 9, true);
        }
        if(identifier.equals("top_coins_value_9")) {
            return plugin.getStatsManager().getTopCoins( 9, false);
        }
        if(identifier.equals("top_coins_name_10")) {
            return plugin.getStatsManager().getTopCoins( 10, true);
        }
        if(identifier.equals("top_coins_value_10")) {
            return plugin.getStatsManager().getTopCoins( 10, false);
        }
        if(identifier.equals("top_coins_name_11")) {
            return plugin.getStatsManager().getTopCoins( 11, true);
        }
        if(identifier.equals("top_coins_value_11")) {
            return plugin.getStatsManager().getTopCoins( 11, false);
        }
        if(identifier.equals("top_coins_name_12")) {
            return plugin.getStatsManager().getTopCoins( 12, true);
        }
        if(identifier.equals("top_coins_value_12")) {
            return plugin.getStatsManager().getTopCoins( 12, false);
        }
        if(identifier.equals("top_coins_name_13")) {
            return plugin.getStatsManager().getTopCoins( 13, true);
        }
        if(identifier.equals("top_coins_value_13")) {
            return plugin.getStatsManager().getTopCoins( 13, false);
        }
        if(identifier.equals("top_coins_name_14")) {
            return plugin.getStatsManager().getTopCoins( 14, true);
        }
        if(identifier.equals("top_coins_value_14")) {
            return plugin.getStatsManager().getTopCoins( 14, false);
        }
        if(identifier.equals("top_coins_name_15")) {
            return plugin.getStatsManager().getTopCoins( 15, true);
        }
        if(identifier.equals("top_coins_value_15")) {
            return plugin.getStatsManager().getTopCoins( 15, false);
        }
        UUID uuid = player.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        if (identifier.equals("player_coins")) {
            return String.valueOf(clanPlayer.getCoins());
        }
        Clan clan = clanPlayer.getClan();
        if (clan == null) {
            return plugin.getFileUtils().language.getString("translate.clan");
        }
        if (identifier.equals("clan_time")) {
            return plugin.getStatsManager().getTime(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - clan.getTimeStamp().getTime()));
        }
        if (identifier.equals("clan_id")) {
            return String.valueOf(clan.getID());
        }
        if (identifier.equals("clan")) {
            return clan.getName();
        }
        if (identifier.equals("cost_upgrade")) {
            return String.valueOf(plugin.getClanManager().getUpgradeCost(clan.getLevel() + 1));
        }
        if (identifier.equals("cost_upgrade_all")) {
            return String.valueOf(plugin.getClanManager().getUpgradeCostAll(clan.getLevel() + 1));
        }
        if (identifier.equals("cost_rename")) {
            return String.valueOf(plugin.getClanManager().getRenameCost(clan.getLevel()));
        }
        if (identifier.equals("cost_rename_all")) {
            return String.valueOf(plugin.getClanManager().getRenameCostAll(clan.getLevel()));
        }
        if (identifier.equals("cost_member")) {
            return String.valueOf(plugin.getClanManager().getMemberCost(clan.getLevel()));
        }
        if (identifier.equals("cost_member_all")) {
            return String.valueOf(plugin.getClanManager().getMemberCostAll(clan.getLevel()));
        }
        if (identifier.equals("clan_leader")) {
            return plugin.getClanPlayer(clan.getLeader()).getName();
        }
        if (identifier.equals("coins")) {
            return String.valueOf(clan.getStats(uuid).getCoins());
        }
        if (identifier.equals("xp")) {
            return String.valueOf(clan.getStats(uuid).getXp());
        }
        if (identifier.equals("kills")) {
            return String.valueOf(clan.getStats(uuid).getKills());
        }
        if (identifier.equals("deaths")) {
            return String.valueOf(clan.getStats(uuid).getDeaths());
        }
        if (identifier.equals("clan_coins")) {
            return String.valueOf(clan.getCoins());
        }
        if (identifier.equals("clan_xp")) {
            return String.valueOf(clan.getXp());
        }
        if (identifier.equals("clan_kills")) {
            return String.valueOf(clan.getKills());
        }
        if (identifier.equals("clan_deaths")) {
            return String.valueOf(clan.getDeaths());
        }
        if (identifier.equals("clan_level")) {
            return String.valueOf(clan.getLevel());
        }
        if (identifier.equals("clan_members")) {
            return String.valueOf(clan.getMembers().size());
        }
        if (identifier.equals("clan_max_members")) {
            return String.valueOf(clan.getMaxMembers());
        }
        if (identifier.equals("share")) {
            return plugin.getStatsManager().getClanShare(clanPlayer);
        }
        return null;
    }
}