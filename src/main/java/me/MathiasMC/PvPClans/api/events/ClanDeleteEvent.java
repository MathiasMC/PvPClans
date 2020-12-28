package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanDeleteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final PvPClans plugin;

    private boolean cancelled = false;

    private final Clan clan;

    public ClanDeleteEvent(Clan clan) {
        this.plugin = PvPClans.getInstance();
        this.clan = clan;
    }

    public Clan getClan() {
        return clan;
    }

    public void execute() {
        plugin.database.deleteClan(clan);
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
