package me.MathiasMC.PvPClans.perks;

import me.MathiasMC.PvPClans.api.Perk;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoinsBonusPerk extends Perk {

    @Override
    public String getIdentifier() {
        return "coins-bonus";
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.GLOWSTONE_DUST);
    }

    @Override
    public String getName() {
        return "&8( &cCoins Bonus &8)";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(" ", "&8> &7( &c{value}&7% ) more.", " ", "&8> &7Enabled {enabled}", " ");
    }

    @Override
    public List<String> getLeader() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lLeader Info&7] &f&l» &7Perk ( &cCoins Bonus &7) is now upgraded.");
    }

    @Override
    public List<String> getModerator() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lModerator Info&7] &f&l» &7Perk ( &cCoins Bonus &7) is now upgraded.");
    }

    @Override
    public List<String> getMember() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lMember Info&7] &f&l» &7Perk ( &cCoins Bonus &7) is now upgraded.");
    }

    @Override
    public List<String> getLeaderRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lLeader Info&7] &f&l» &7You have lost perk ( &cCoins Bonus &7)");
    }

    @Override
    public List<String> getModeratorRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lModerator Info&7] &f&l» &7You have lost perk ( &cCoins Bonus &7)");
    }

    @Override
    public List<String> getMemberRemove() {
        return Collections.singletonList("pvpclans message {player} &7[&6&lMember Info&7] &f&l» &7You have lost perk ( &cCoins Bonus &7)");
    }

    @Override
    public String onRequest(ClanPlayer clanPlayer, String placeholder) {
        return null;
    }
}