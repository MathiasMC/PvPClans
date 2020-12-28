package me.MathiasMC.PvPClans.gui;

import me.MathiasMC.PvPClans.data.ClanPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Menu {

    private final Player player;

    private final UUID uuid;

    private ClanPlayer clanTarget;

    private String rename;

    private final HashMap<Integer, UUID> players = new HashMap<>();

    private final HashMap<Integer, String> perks = new HashMap<>();

    public Menu(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public ClanPlayer getClanTarget() {
        return clanTarget;
    }

    public String getRename() {
        return rename;
    }

    public void setClanTarget(ClanPlayer clanTarget) {
        this.clanTarget = clanTarget;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public HashMap<Integer, UUID> getPlayers() {
        return players;
    }

    public HashMap<Integer, String> getPerks() {
        return perks;
    }
}