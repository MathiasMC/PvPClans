package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.data.ClanStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeath implements Listener {

    private final PvPClans plugin;

    public PlayerDeath(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        UUID uuid = player.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        if (plugin.getStatsManager().canProgress(player)) {
            Clan clan = clanPlayer.getClan();
            if (clan == null) return;
            ClanStats clanStats = clanPlayer.getStats();
            clanStats.setDeaths(clanStats.getDeaths() + 1);
            clanStats.requestSave();
        }
        Player killer = player.getKiller();
        if (killer != null) {
            if (plugin.getStatsManager().canProgress(killer)) {
                final UUID uuidKiller = killer.getUniqueId();
                final ClanPlayer clanKiller = plugin.getClanPlayer(uuidKiller);
                if (!plugin.getSessionManager().hasSession(clanKiller, clanPlayer)) {
                    Clan clan = clanKiller.getClan();
                    if (clan == null) return;
                    ClanStats clanStats = clanKiller.getStats();
                    clanStats.setKills(clanStats.getKills() + 1);
                    clanStats.requestSave();
                }
            }
        }
    }
}
