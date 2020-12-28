package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.events.ClanKickEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class KickGUI extends GUI {

    public KickGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().confirmKick;
    }

    @Override
    public boolean perform(String text) {
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        if (clan == null) return false;
        ClanPlayer clanTarget = playerMenu.getClanTarget();
        if (clanTarget == null) return false;
        ClanKickEvent clanKickEvent = new ClanKickEvent(clanPlayer, clanTarget);
        plugin.getServer().getPluginManager().callEvent(clanKickEvent);
        if (clanKickEvent.isCancelled()) return false;
        Utils.dispatchCommandList(clanTarget, clanPlayer, "gui.kick.target");
        Utils.alertClan(clan, clanPlayer, clanTarget, "gui.kick");
        clanKickEvent.execute();
        clan.saveAsync();
        clanTarget.saveAsync();
        return true;
    }
}