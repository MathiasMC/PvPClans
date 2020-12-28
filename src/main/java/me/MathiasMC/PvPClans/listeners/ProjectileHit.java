package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.concurrent.ThreadLocalRandom;

public class ProjectileHit implements Listener {

    private final PvPClans plugin;

    public ProjectileHit(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) return;
        if (!(e.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getEntity();
        Player player = (Player) arrow.getShooter();
        ClanPlayer clanPlayer = plugin.getClanPlayer(player.getUniqueId());
        Clan clan = clanPlayer.getClan();
        if (clan == null) return;
        String identifier = "explosive-arrow";
        if (!clan.hasPerk(identifier)) return;
        if (!clan.isPerkActive(identifier)) return;
        int[] value = clan.getPerk(identifier);
        int amount = value[0];
        if (amount != 1) {
            if (amount == 0) amount = 1;
            int random = ThreadLocalRandom.current().nextInt(1, (amount + 1));
            if (random != 1) return;
        }
        Location location = arrow.getLocation();
        Perk perk = plugin.getPerks().get(identifier);
        double radius = perk.getConfig().getDouble(value[1] + ".damage.radius", 3);
        double damage = Utils.randomDouble(perk.getConfig().getDouble(value[1] + ".damage.min", 1.5), perk.getConfig().getDouble(value[1] + ".damage.max", 3));
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(damage);
            }
        }
        location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(),
                (float) perk.getConfig().getDouble(value[1] + ".radius", 0.6F),
                perk.getConfig().getBoolean(value[1] + ".fire", false),
                perk.getConfig().getBoolean(value[1] + ".break", false));
    }
}
