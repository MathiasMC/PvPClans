package me.MathiasMC.PvPClans.perks;

import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExplosiveArrowPerk extends Perk {
    @Override
    public String getIdentifier() {
        return "explosive-arrow";
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public String getName() {
        return "&8( &cExplosive Arrow &8)";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(" ", "&8> &7( &c1 &7out of &c{value}&7 ) chance to explode on hit.", " ", "&8> &7Damage", "&8> &7Min &c{damage_min}", "&8> &7Max &c{damage_max}", " ", "&8> &7Enabled {enabled}", " ");
    }

    @Override
    public List<String> getLeader() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lLeader Info&7] &f&l» &7Perk ( &cExplosive Arrow &7) is now upgraded.");
    }

    @Override
    public List<String> getModerator() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lModerator Info&7] &f&l» &7Perk ( &cExplosive Arrow &7) is now upgraded.");
    }

    @Override
    public List<String> getMember() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lMember Info&7] &f&l» &7Perk ( &cExplosive Arrow &7) is now upgraded.");
    }

    @Override
    public List<String> getLeaderRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lLeader Info&7] &f&l» &7You have lost perk ( &cExplosive Arrow &7)");
    }

    @Override
    public List<String> getModeratorRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lModerator Info&7] &f&l» &7You have lost perk ( &cExplosive Arrow &7)");
    }

    @Override
    public List<String> getMemberRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lMember Info&7] &f&l» &7You have lost perk ( &cExplosive Arrow &7)");
    }

    @Override
    public void setupConfig() {
        getConfig().set("1.radius", 0.4);
        getConfig().set("1.damage.radius", 1.0);
        getConfig().set("1.damage.min", 1.5);
        getConfig().set("1.damage.max", 3.0);
        getConfig().set("1.fire", false);
        getConfig().set("1.break", false);
        getConfig().set("2.radius", 0.6);
        getConfig().set("2.damage.radius", 2.0);
        getConfig().set("2.damage.min", 2.0);
        getConfig().set("2.damage.max", 3.0);
        getConfig().set("2.fire", false);
        getConfig().set("2.break", false);
        getConfig().set("3.radius", 0.8);
        getConfig().set("3.damage.radius", 3.0);
        getConfig().set("3.damage.min", 2.5);
        getConfig().set("3.damage.max", 4.0);
        getConfig().set("3.fire", false);
        getConfig().set("3.break", false);
    }

    @Override
    public String onRequest(ClanPlayer clanPlayer, String placeholder) {
        if (placeholder.equals("damage_min")) {
            return String.valueOf(getConfig().getDouble(clanPlayer.getClan().getPerk(getIdentifier())[1] + ".damage.min", 1));
        }
        if (placeholder.equals("damage_max")) {
            return String.valueOf(getConfig().getDouble(clanPlayer.getClan().getPerk(getIdentifier())[1] + ".damage.max", 1));
        }
        return null;
    }
}
