package me.MathiasMC.PvPClans.gui.menu;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.events.ClanMemberEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class MemberGUI extends GUI {

    public MemberGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().confirmMember;
    }

    @Override
    public boolean perform(String text) {
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        if (clan == null) return false;
        ClanPlayer clanTarget = playerMenu.getClanTarget();
        if (clanTarget == null) return false;
        ClanMemberEvent event = new ClanMemberEvent(clanTarget);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;
        Utils.alertClan(clan, clanPlayer, clanTarget, "gui.member");
        event.execute();
        clan.requestSave();
        return true;
    }
}
