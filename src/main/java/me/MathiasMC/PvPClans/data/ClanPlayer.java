package me.MathiasMC.PvPClans.data;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.api.Response;

import java.util.UUID;

public class ClanPlayer {

    private final UUID uuid;

    private final String name;

    private long clanID;

    private long coins;

    private Response.WithdrawType response = Response.WithdrawType.OWN;

    public ClanPlayer(UUID uuid) {
        this.uuid = uuid;
        this.name = PvPClans.getInstance().getServer().getOfflinePlayer(uuid).getName();
        long[] data = PvPClans.getInstance().database.getClanPlayer(uuid);
        this.clanID = data[0];
        this.coins = data[1];
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return name;
    }

    public long getClanID() {
        return clanID;
    }

    public long getCoins() {
        return coins;
    }

    public Clan getClan() {
        return PvPClans.getInstance().getClan(clanID);
    }

    public ClanStats getStats() {
        return getClan().getStats(uuid);
    }

    public Response.WithdrawType getResponse() {
        return response;
    }

    public void setClanID(long clanID) {
        this.clanID = clanID;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public void setResponse(Response.WithdrawType response) {
        this.response = response;
    }

    public void requestSave() {
        PvPClans.getInstance().getSaveTask().getPlayer().add(uuid);
    }

    public void saveAsync() {
        PvPClans.getInstance().database.setClanPlayer(uuid, clanID, coins);
    }
}