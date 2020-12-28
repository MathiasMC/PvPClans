package me.MathiasMC.PvPClans.task;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;

public class SaveTask extends BukkitRunnable {

    private final PvPClans plugin;

    private final LinkedHashSet<Long> clans = new LinkedHashSet<>();

    private final LinkedHashSet<UUID> stats = new LinkedHashSet<>();

    private final LinkedHashSet<UUID> player = new LinkedHashSet<>();

    public SaveTask(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Iterator<Long> clans = new ArrayList<>(this.clans).iterator();
        Iterator<UUID> stats = new ArrayList<>(this.stats).iterator();
        Iterator<UUID> player = new ArrayList<>(this.player).iterator();
        while (clans.hasNext()) {
            long id = clans.next();
            if (plugin.getClans().containsKey(id)) {
                plugin.getClan(id).saveAsync();
            }
            this.clans.remove(id);
        }
        while (stats.hasNext()) {
            UUID uuid = stats.next();
            Clan clan = plugin.getClan(uuid);
            if (clan != null) {
                clan.getStats(uuid).saveAsync();
            }
            this.stats.remove(uuid);
        }
        while (player.hasNext()) {
            UUID uuid = player.next();
            plugin.getClanPlayer(uuid).saveAsync();
            this.player.remove(uuid);
        }
    }

    public LinkedHashSet<Long> getClans() {
        return clans;
    }

    public LinkedHashSet<UUID> getStats() {
        return stats;
    }

    public LinkedHashSet<UUID> getPlayer() {
        return player;
    }
}
