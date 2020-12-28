package me.MathiasMC.PvPClans.listeners;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.gui.menu.market.confirm.ConfirmRenameGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChat implements Listener {

    private final PvPClans plugin;

    public AsyncPlayerChat(final PvPClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getRenameClan().contains(uuid)) return;
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        if (clan == null) return;
        String clanName = e.getMessage();
        clan.setRename(clanName);
        Menu menu = plugin.getPlayerMenu(player);
        menu.setRename(clanName);
        new ConfirmRenameGUI(menu).open();
        e.setCancelled(true);
    }
}
