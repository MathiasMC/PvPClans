package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanRemoveXpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final ClanStats clanStats;

    private final long xp;

    public ClanRemoveXpEvent(ClanStats clanStats, long xp) {
        this.clanStats = clanStats;
        this.xp = xp;
    }

    public ClanStats getClanStats() {
        return clanStats;
    }

    public long getXp() {
        return xp;
    }

    public void execute() {
        clanStats.setXp(clanStats.getXp() - xp);
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