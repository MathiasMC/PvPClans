package me.MathiasMC.PvPClans.managers;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.api.events.ClanWithdrawEvent;
import me.MathiasMC.PvPClans.api.events.ClanWithdrawMemberEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.configuration.file.FileConfiguration;

public class ClanManager {

    private final PvPClans plugin;

    private final FileConfiguration config;

    public ClanManager(final PvPClans plugin) {
        this.plugin = plugin;
        this.config = plugin.getFileUtils().config;
    }

    public long getRenameCost(ClanPlayer clanPlayer) {
        long level = clanPlayer.getClan().getLevel();
        if (clanPlayer.getResponse() == Response.WithdrawType.CLAN) {
            return getRenameCostAll(level);
        }
        return getRenameCost(level);
    }

    public long getMemberCost(ClanPlayer clanPlayer) {
        long level = clanPlayer.getClan().getLevel();
        if (clanPlayer.getResponse() == Response.WithdrawType.CLAN) {
            return getMemberCostAll(level);
        }
        return getMemberCost(level);
    }

    public long getUpgradeCost(ClanPlayer clanPlayer) {
        long level = (clanPlayer.getClan().getLevel() + 1);
        if (clanPlayer.getResponse() == Response.WithdrawType.CLAN) {
            return getUpgradeCostAll(level);
        }
        return getUpgradeCost(level);
    }

    public long getRenameCostAll(long level) {
        return config.getLong("cost.rename." + level + ".all", config.getLong("cost.rename.all", 0));
    }

    public long getRenameCost(long level) {
        return config.getLong("cost.rename." + level + ".player",config.getLong("cost.rename.player", 0));
    }

    public long getMemberCost(long level) {
        return config.getLong("cost.member." + level + ".player", config.getLong("cost.member.player", 0));
    }

    public long getMemberCostAll(long level) {
        return config.getLong("cost.member." + level + ".all", config.getLong("cost.member.all", 0));
    }

    public long getUpgradeCost(long level) {
        return config.getLong("cost.upgrade." + level + ".player", config.getLong("cost.upgrade.player", 0));
    }

    public long getUpgradeCostAll(long level) {
        return config.getLong("cost.upgrade." + level + ".all", config.getLong("cost.upgrade.all", 0));
    }

    public Response.Withdraw withdraw(ClanPlayer clanPlayer, long cost) {
        if (clanPlayer.getResponse() == Response.WithdrawType.CLAN) return withdrawClan(clanPlayer.getClan(), cost);
        return withdrawPlayer(clanPlayer.getStats(), cost);
    }

    public boolean canWithdraw(ClanPlayer clanPlayer, long cost) {
        if (clanPlayer.getResponse() == Response.WithdrawType.CLAN) return canWithdrawClan(clanPlayer.getClan(), cost);
        return canWithdrawPlayer(clanPlayer.getStats(), cost);
    }

    public boolean canWithdrawPlayer(ClanStats clanStats, long cost) {
        return clanStats.getCoins() - cost >= 0;
    }

    public boolean canWithdrawClan(Clan clan, long cost) {
        return clan.getCoins() - cost >= 0;
    }

    public Response.Withdraw withdrawPlayer(ClanStats clanStats, long cost) {
        ClanWithdrawEvent event = new ClanWithdrawEvent(clanStats, cost);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return Response.Withdraw.CANCELLED;
        if (event.getResponse() == Response.Withdraw.ENOUGH) return Response.Withdraw.ENOUGH;
        event.execute();
        return Response.Withdraw.SUCCESS;
    }

    public Response.Withdraw withdrawClan(Clan clan, long cost) {
        ClanWithdrawMemberEvent event = new ClanWithdrawMemberEvent(clan, cost);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return Response.Withdraw.CANCELLED;
        if (event.getResponse() == Response.Withdraw.ENOUGH) return Response.Withdraw.ENOUGH;
        event.execute();
        return Response.Withdraw.SUCCESS;
    }
}
