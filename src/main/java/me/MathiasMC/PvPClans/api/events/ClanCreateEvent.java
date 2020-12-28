package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanCreateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final PvPClans plugin;

    private boolean cancelled = false;

    private final ClanPlayer clanPlayer;

    private final String name;

    private final Response.Create response;

    private final long id;

    private final long cost;

    public ClanCreateEvent(ClanPlayer clanPlayer, String name, long cost) {
        this.plugin = PvPClans.getInstance();
        this.clanPlayer = clanPlayer;
        this.name = name;
        this.cost = cost;
        if (clanPlayer.getCoins() < cost) {
            this.response = Response.Create.ENOUGH;
            this.id = 0;
        } else if (plugin.clanNames.contains(name)) {
            this.response = Response.Create.EXISTS;
            this.id = 0;
        } else if (Utils.isValidClan(name)) {
            this.response = Response.Create.SUCCESS;
            this.id = plugin.getNewID();
        } else {
            this.response = Response.Create.NAME;
            this.id = 0;
        }
    }

    public ClanPlayer getClanPlayer() {
        return clanPlayer;
    }

    public String getName() {
        return name;
    }

    public Response.Create getResponse() {
        return response;
    }

    public long getID() {
        return id;
    }

    public long getCost() {
        return cost;
    }

    public void execute() {
        plugin.database.insertClan(clanPlayer.getUniqueId(), id, name);
        clanPlayer.setCoins(clanPlayer.getCoins() - cost);
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
