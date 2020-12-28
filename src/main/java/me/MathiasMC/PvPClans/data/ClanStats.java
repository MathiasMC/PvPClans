package me.MathiasMC.PvPClans.data;

import me.MathiasMC.PvPClans.PvPClans;

import java.util.UUID;

public class ClanStats {

    private final UUID uuid;

    private final long clanID;

    private long coins;

    private long setShare;

    private boolean shareCoins = false;

    private long xp;

    private long kills;

    private long deaths;

    public ClanStats(UUID uuid, long clanID) {
        this.uuid = uuid;
        this.clanID = clanID;
        final long[] data = PvPClans.getInstance().database.getClanStats(uuid, clanID);
        this.coins = data[0];
        this.setShare = data[1];
        if (setShare == 1) {
            this.shareCoins = true;
        }
        this.xp = data[2];
        this.kills = data[3];
        this.deaths = data[4];
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public long getCoins() {
        return coins;
    }

    public boolean isShareCoins() {
        return shareCoins;
    }

    public long getXp() {
        return xp;
    }

    public long getKills() {
        return kills;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public void setShareCoins(boolean share) {
        this.shareCoins = share;
        if (share) {
            setShare = 1;
        } else {
            setShare = 0;
        }
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void requestSave() {
        PvPClans.getInstance().getSaveTask().getStats().add(uuid);
    }

    public void saveAsync() {
        PvPClans.getInstance().database.setClanStats(uuid, clanID, coins, setShare, xp, kills, deaths);
    }
}