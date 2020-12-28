package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanWithdrawEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final ClanStats clanStats;

    private long amount;

    private final  Response.Withdraw response;

    private final long set;

    public ClanWithdrawEvent(ClanStats clanStats, long amount) {
        this.clanStats = clanStats;
        this.amount = amount;
        this.set = clanStats.getCoins() - amount;
        if (set < 0) {
            this.response = Response.Withdraw.ENOUGH;
            return;
        }
        this.response = Response.Withdraw.SUCCESS;
    }

    public ClanStats getClanStats() {
        return clanStats;
    }

    public long getAmount() {
        return amount;
    }

    public Response.Withdraw getResponse() {
        return response;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void execute() {
        clanStats.setCoins(set);
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