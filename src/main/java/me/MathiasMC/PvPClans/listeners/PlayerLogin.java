package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class PlayerLogin implements Listener {

    private final PvPClans plugin;

    public PlayerLogin(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent e) {
        final UUID playerUUID = e.getPlayer().getUniqueId();
        plugin.database.insertClanPlayer(playerUUID);
        plugin.getClanPlayer(playerUUID);
    }
}