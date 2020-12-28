package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.data.Clan;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanMemberSizeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final Clan clan;

    private final long amount;

    public ClanMemberSizeEvent(Clan clan, long amount) {
        this.clan = clan;
        this.amount = amount;
    }

    public Clan getClan() {
        return clan;
    }

    public long getAmount() {
        return amount;
    }

    public void execute() {
        clan.setMaxMembers(amount);
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
