package me.MathiasMC.PvPClans.gui.menu.market.confirm;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;
import me.MathiasMC.PvPClans.api.events.ClanMemberSizeEvent;
import me.MathiasMC.PvPClans.data.Clan;
import me.MathiasMC.PvPClans.data.ClanPlayer;
import me.MathiasMC.PvPClans.gui.GUI;
import me.MathiasMC.PvPClans.gui.Menu;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class ConfirmMemberSizeGUI extends GUI {

    public ConfirmMemberSizeGUI(Menu playerMenu) {
        super(playerMenu);
    }

    @Override
    public FileConfiguration getFile() {
        return PvPClans.getInstance().getFileUtils().confirmMemberSize;
    }

    @Override
    public boolean perform(String text) {
        UUID uuid = playerMenu.getUniqueId();
        ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
        Clan clan = clanPlayer.getClan();
        Response.Withdraw withdraw = plugin.getClanManager().withdraw(clanPlayer, plugin.getClanManager().getMemberCost(clanPlayer));
        switch (withdraw) {
            case ENOUGH:
                Utils.dispatchCommandList(clanPlayer, "gui.member-size.enough");
                break;
            case CANCELLED:
                return false;
        }
        ClanMemberSizeEvent clanMemberSizeEvent = new ClanMemberSizeEvent(clan, clan.getMaxMembers() + 1);
        plugin.getServer().getPluginManager().callEvent(clanMemberSizeEvent);
        if (clanMemberSizeEvent.isCancelled()) {
            return false;
        }
        clanMemberSizeEvent.execute();
        clan.saveAsync();
        Utils.alertClan(clan, clanPlayer, clanPlayer, "gui.member-size");
        return true;
    }
}