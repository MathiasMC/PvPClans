package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.data.Clan;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanUpgradeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final Clan clan;

    private final long level;

    public ClanUpgradeEvent(Clan clan, long level) {
        this.clan = clan;
        this.level = level;
    }

    public Clan getClan() {
        return clan;
    }

    public long getLevel() {
        return level;
    }

    public void execute() {
        clan.setLevel(level);
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
