package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuit implements Listener {

    private final PvPClans plugin;

    public PlayerQuit(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        plugin.getPlayerMenu().remove(player);
        plugin.getInvites().remove(playerUUID);
        plugin.getRenameClan().remove(playerUUID);
        Clan clan = plugin.getClan(playerUUID);
        if (clan == null) return;
        clan.setTimeStamp();
        clan.saveAsync();
    }
}
