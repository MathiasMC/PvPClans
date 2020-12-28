package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanAcceptEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    private final ClanPlayer clanPlayer;

    private final ClanPlayer clanTarget;

    private final Clan clan;

    private final Response.Accept response;

    public ClanAcceptEvent(ClanPlayer clanPlayer, ClanPlayer clanTarget) {
        this.clanPlayer = clanPlayer;
        this.clanTarget = clanTarget;
        this.clan = clanTarget.getClan();
        if (clan.isMemberLimit()) {
            this.response = Response.Accept.LIMIT;
        } else {
            this.response = Response.Accept.SUCCESS;
        }
    }

    public ClanPlayer getClanPlayer() {
        return clanPlayer;
    }

    public ClanPlayer getClanTarget() {
        return clanTarget;
    }

    public Clan getClan() {
        return clan;
    }

    public Response.Accept getResponse() {
        return response;
    }

    public void execute() {
        clan.addMember(clanPlayer.getUniqueId());
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