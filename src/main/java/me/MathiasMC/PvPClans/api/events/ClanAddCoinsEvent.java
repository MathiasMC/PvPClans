package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanAddCoinsEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final ClanStats clanStats;

    private final long coins;

    public ClanAddCoinsEvent(ClanStats clanStats, long coins) {
        this.clanStats = clanStats;
        this.coins = coins;
    }

    public ClanStats getClanStats() {
        return clanStats;
    }

    public long getCoins() {
        return this.coins;
    }

    public void execute() {
        clanStats.setCoins(clanStats.getCoins() + coins);
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