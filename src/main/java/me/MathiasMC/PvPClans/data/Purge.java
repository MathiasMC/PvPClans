package me.MathiasMC.PvPClans.data;

import me.MathiasMC.PvPClans.PvPClans;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Purge {

    private final PvPClans plugin;

    public Purge(final PvPClans plugin) {
        this.plugin = plugin;
        long interval = plugin.getFileUtils().config.getInt("mysql.purge.interval");
        long startInterval = interval;
        if (plugin.getFileUtils().config.getBoolean("mysql.purge.check-on-startup")) {
            startInterval = 1;
        }
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Clan clan : plugin.getClans().values()) {
                if (isOld(clan.getTimeStamp())) {
                    plugin.database.deleteClan(clan);
                }
            }
        }, startInterval * 20, interval * 72000);
    }

    private boolean isOld(Timestamp date) {
        return ChronoUnit.DAYS.between(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) > plugin.getFileUtils().config.getInt("mysql.purge.inactive-days");
    }
}