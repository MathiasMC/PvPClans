package me.MathiasMC.PvPClans.gui.menu.market;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.gui.menu.market.confirm.ConfirmMemberSizeGUI;
import me.MathiasMC.PvPClans.managers.ClanManager;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class MemberSizeGUI extends GUI {

    public MemberSizeGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().memberSize;
    }

    @Override
    public boolean perform(String text) {
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        if (clan == null) return false;
        if (clan.getMaxMembers() >= plugin.getFileUtils().clan.getStringList("member.slot").size()) {
            Utils.dispatchCommandList(clanPlayer, "gui.member-size.limit");
            return false;
        }
        ClanManager clanManager = plugin.getClanManager();
        long cost = clanManager.getMemberCost(clanPlayer);
        if (!clanManager.canWithdraw(clanPlayer, cost)) {
            Utils.dispatchCommandList(clanPlayer, "gui.member-size.enough");
            return false;
        }
        new ConfirmMemberSizeGUI(playerMenu).open();
        return true;
    }
}