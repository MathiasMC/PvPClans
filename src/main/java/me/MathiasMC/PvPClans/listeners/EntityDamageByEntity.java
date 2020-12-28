package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntityDamageByEntity implements Listener {

    private final PvPClans plugin;

    public EntityDamageByEntity(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Arrow) {
            final Arrow arrow = (Arrow) damager;
            if (arrow.getShooter() instanceof Player) {
                damager = (Player) arrow.getShooter();
            }
        }
        if (!(entity instanceof Player)) return;
        if (!(damager instanceof Player)) return;
        if (!plugin.getStatsManager().canProgress((Player) damager)) return;
        Clan clan = plugin.getClanPlayer(damager.getUniqueId()).getClan();
        if (clan == null) return;
        String identifier = "damage";
        if (!clan.hasPerk(identifier)) return;
        if (!clan.isPerkActive(identifier)) return;
        UUID uuid = entity.getUniqueId();
        if (!clan.getLeader().equals(uuid) && !clan.getMembers().contains(uuid)) return;
        int random = Utils.getRandom().nextInt(100);
        if (random <= clan.getPerk(identifier)[0]) {
            e.setCancelled(true);
        }
    }
}