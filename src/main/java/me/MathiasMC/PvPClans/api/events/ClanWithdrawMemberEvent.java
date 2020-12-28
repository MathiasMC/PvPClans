package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.*;

public class ClanWithdrawMemberEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final Clan clan;

    private long amount;

    private final Response.Withdraw response;

    private final HashMap<UUID, Long> players = new HashMap<>();

    public ClanWithdrawMemberEvent(Clan clan, long amount) {
        this.clan = clan;
        this.amount = amount;
        long set = clan.getCoins() - amount;
        if (set < 0) {
            this.response = Response.Withdraw.ENOUGH;
            return;
        }
        List<UUID> members = new ArrayList<>(clan.getMembers());
        members.add(clan.getLeader());
        TreeMap<Long, UUID> map = new TreeMap<>();
        for (UUID uuid : members) {
            ClanStats clanStats = clan.getStats(uuid);
            if (clanStats.isShareCoins()) {
                map.put(clanStats.getCoins(), uuid);
            }
        }
        long take = amount / map.size();
        long rest = 0;
        for (Long key : map.keySet()) {
            long a = key - (take + rest);
            if (a >= 0) {
                players.put(map.get(key), a);
                rest = 0;
            } else {
                rest = Math.abs(a);
                players.put(map.get(key), 0L);
            }
        }
        this.response = Response.Withdraw.SUCCESS;
    }

    public Clan getClan() {
        return clan;
    }

    public long getAmount() {
        return amount;
    }

    public Response.Withdraw getResponse() {
        return response;
    }

    public HashMap<UUID, Long> getPlayers() {
        return players;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void execute() {
        for (UUID uuid : players.keySet()) {
            ClanStats clanStats = clan.getStats(uuid);
            clanStats.setCoins(players.get(uuid));
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean set) {
        cancelled = set;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
