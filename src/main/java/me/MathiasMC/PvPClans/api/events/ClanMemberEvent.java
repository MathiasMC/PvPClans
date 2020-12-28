package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanMemberEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final ClanPlayer clanPlayer;

    private final Clan clan;

    public ClanMemberEvent(ClanPlayer clanPlayer) {
        this.clanPlayer = clanPlayer;
        this.clan = clanPlayer.getClan();
    }

    public Clan getClan() {
        return clan;
    }

    public void execute() {
        clan.removeModerator(clanPlayer.getUniqueId());
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