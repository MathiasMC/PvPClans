package me.MathiasMC.PvPClans.api.events;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClanRenameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final PvPClans plugin;

    private boolean cancelled = false;

    private final Clan clan;

    private final String name;

    private final String oldName;

    private final Response.Rename response;

    public ClanRenameEvent(Clan clan, String name) {
        this.plugin = PvPClans.getInstance();
        this.clan = clan;
        this.name = name;
        this.oldName = clan.getName();
        if (plugin.clanNames.contains(name)) {
            this.response = Response.Rename.EXISTS;
        } else if (Utils.isValidClan(name)) {
            this.response = Response.Rename.SUCCESS;
        } else {
            this.response = Response.Rename.NAME;
        }
    }

    public Clan getClan() {
        return clan;
    }

    public String getName() {
        return name;
    }

    public String getOldName() {
        return oldName;
    }

    public Response.Rename getResponse() {
        return response;
    }

    public void execute() {
        plugin.clanNames.add(name);
        plugin.clanNames.remove(oldName);
        plugin.getRenameClan().remove(clan.getLeader());
        clan.setName(name);
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