package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanInviteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final PvPClans plugin;

    private boolean cancelled = false;

    private final ClanPlayer clanPlayer;

    private final ClanPlayer clanTarget;

    private final Response.Invite response;

    public ClanInviteEvent(ClanPlayer clanPlayer, ClanPlayer clanTarget) {
        this.plugin = PvPClans.getInstance();
        this.clanPlayer = clanPlayer;
        this.clanTarget = clanTarget;
        if (clanPlayer.getClan().isMemberLimit()) {
            this.response = Response.Invite.LIMIT;
        } else {
            this.response = Response.Invite.SUCCESS;
        }
    }

    public ClanPlayer getClanPlayer() {
        return clanPlayer;
    }

    public ClanPlayer getClanTarget() {
        return clanTarget;
    }

    public Response.Invite getResponse() {
        return response;
    }

    public void execute() {
        plugin.getInvites().put(clanTarget.getUniqueId(), clanPlayer.getUniqueId());
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