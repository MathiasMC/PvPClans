package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ClanGUI extends GUI {

    UUID uuid = playerMenu.getUniqueId();
    ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
    Clan clan = clanPlayer.getClan();

    public ClanGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().clan;
    }

    @Override
    public void setItems() {
        UUID leaderUUID = clan.getLeader();
        int leaderSlot = file.getInt("leader.slot");
        List<String> headSlot = file.getStringList("member.slot");
        List<UUID> moderators = clan.getModerators();
        List<UUID> remaining = new ArrayList<>(clan.getMembers());
        remaining.removeAll(moderators);
        int id = 0;
        String moderatorName = file.getString("moderator.name");
        List<String> moderatorLore = file.getStringList("moderator.lore");
        String memberName = file.getString("member.name");
        List<String> memberLore = file.getStringList("member.lore");
        plugin.getItemManager().addHead(inventory, leaderSlot,
                plugin.getClanPlayer(leaderUUID),
                file.getString("leader.name"),
                file.getStringList("leader.lore"));
        playerMenu.getPlayers().put(leaderSlot, leaderUUID);
        for (UUID uuid : moderators) {
            int slot = Integer.parseInt(headSlot.get(id));
            plugin.getItemManager().addHead(inventory, slot,
                    plugin.getClanPlayer(uuid),
                    moderatorName,
                    moderatorLore);
            playerMenu.getPlayers().put(slot, uuid);
            id++;
        }
        for (UUID uuid : remaining) {
            int slot = Integer.parseInt(headSlot.get(id));
            plugin.getItemManager().addHead(inventory, slot,
                    plugin.getClanPlayer(uuid),
                    memberName,
                    memberLore);
            playerMenu.getPlayers().put(slot, uuid);
            id++;
        }
    }
}
