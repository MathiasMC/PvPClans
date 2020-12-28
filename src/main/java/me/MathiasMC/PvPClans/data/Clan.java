package me.MathiasMC.PvPClans.data;

import me.MathiasMC.PvPClans.PvPClans;

import java.sql.Timestamp;
import java.util.*;

public class Clan {

    private final long id;

    private String name;

    private String rename = "";

    private String prefix;

    private UUID leader;

    private List<UUID> moderators = new ArrayList<>();

    private final List<UUID> members = new ArrayList<>();

    private long level;

    private long maxMembers;

    private final Map<String, int[]> perks = new HashMap<>();

    private Timestamp timeStamp;

    private final Map<UUID, ClanStats> stats = new HashMap<>();

    public Clan(long id) {
        this.id = id;
        final String[] data = PvPClans.getInstance().database.getClan(id);
        this.name = data[0];
        this.prefix = data[1];
        this.leader = UUID.fromString(data[2]);
        final String moderators = data[3];
        if (moderators.length() > 0) {
            String[] split = moderators.split("\\s*,\\s*");
            for (String moderator : split) {
                this.moderators.add(UUID.fromString(moderator));
            }
        }
        final String members = data[4];
        if (members.length() > 0) {
            String[] split = members.split("\\s*,\\s*");
            for (String member : split) {
                this.members.add(UUID.fromString(member));
            }
        }
        this.level = Long.parseLong(data[5]);
        this.maxMembers = Long.parseLong(data[6]);
        final String perks = data[7];
        if (perks.length() > 0) {
            String[] split = perks.split("\\s*,\\s*");
            for (String perk : split) {
                String[] value = perk.split(":");
                this.perks.put(value[0], new int[]{Integer.parseInt(value[1]), Integer.parseInt(value[2]), Integer.parseInt(value[3])});
            }
        }
        this.timeStamp = Timestamp.valueOf(data[8]);
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRename() {
        return rename;
    }

    public String getPrefix() {
        return prefix;
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getModerators() {
        return moderators;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public long getLevel() {
        return level;
    }

    public long getMaxMembers() {
        return maxMembers;
    }

    public int[] getPerk(String identifier) {
        return perks.get(identifier);
    }

    public Map<String, int[]> getPerks() {
        return perks;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setLeader(UUID playerUUID) {
        this.leader = playerUUID;
    }

    public void setModerators(final List<UUID> moderators) {
        this.moderators = moderators;
    }

    public void setLevel(final long level) {
        this.level = level;
    }

    public void setMaxMembers(final long maxMembers) {
        this.maxMembers = maxMembers;
    }

    public void setTimeStamp() {
        this.timeStamp = new Timestamp(new Date().getTime());
    }

    public boolean isLeader(final UUID playerUUID) {
        return leader.equals(playerUUID);
    }

    public boolean isModerator(final UUID playerUUID) {
        return moderators.contains(playerUUID);
    }

    public boolean isMember(final UUID playerUUID) {
        return members.contains(playerUUID);
    }

    public boolean isMemberLimit() {
        return members.size() >= maxMembers;
    }

    public void addModerator(final UUID playerUUID) {
        moderators.add(playerUUID);
    }

    public void removeModerator(final UUID playerUUID) {
        moderators.remove(playerUUID);
    }

    public void addMember(final UUID playerUUID) {
        ClanPlayer clanPlayer = PvPClans.getInstance().getClanPlayer(playerUUID);
        PvPClans.getInstance().getInvites().remove(playerUUID);
        clanPlayer.setClanID(id);
        members.add(playerUUID);
    }

    public void removeMember(final UUID playerUUID) {
        ClanPlayer clanPlayer = PvPClans.getInstance().getClanPlayer(playerUUID);
        clanPlayer.setClanID(0);
        members.remove(playerUUID);
        moderators.remove(playerUUID);
    }

    public void addPerk(String identifier, int value, int otherValue, boolean enabled) {
        int enable = 1;
        if (!enabled) enable = 0;
        this.perks.put(identifier, new int[]{value, otherValue, enable});
    }

    public boolean hasPerk(String identifier) {
        return this.perks.containsKey(identifier);
    }

    public boolean isPerkActive(String identifier) {
        return this.perks.get(identifier)[2] == 1;
    }

    public void setPerk(String identifier, boolean enabled) {
        int[] value = perks.get(identifier);
        int enable = 1;
        if (!enabled) enable = 0;
        perks.put(identifier, new int[]{value[0], value[1], enable});
    }

    public void removePerk(String identifier) {
        this.perks.remove(identifier);
    }

    public ClanStats getStats(UUID playerUUID) {
        if (stats.containsKey(playerUUID)) {
            return stats.get(playerUUID);
        }
        ClanStats stats = new ClanStats(playerUUID, id);
        this.stats.put(playerUUID, stats);
        return stats;
    }

    public void requestSave() {
        PvPClans.getInstance().getSaveTask().getClans().add(id);
    }

    private String joinModerators() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (UUID member : moderators) {
            stringJoiner.add(member.toString());
        }
        return stringJoiner.toString();
    }

    private String joinMembers() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (UUID member : members) {
            stringJoiner.add(member.toString());
        }
        return stringJoiner.toString();
    }

    private String joinPerks() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String member : perks.keySet()) {
            int[] array = perks.get(member);
            stringJoiner.add(member + ":" + array[0] + ":" + array[1] + ":" + array[2]);
        }
        return stringJoiner.toString();
    }

    public long getCoins() {
        long amount = 0;
        ClanStats lStats = getStats(leader);
        if (lStats.isShareCoins()) {
            amount += lStats.getCoins();
        }
        for (UUID uuid : members) {
            ClanStats clanStats = getStats(uuid);
            if (clanStats.isShareCoins()) {
                amount += clanStats.getCoins();
            }
        }
        return amount;
    }

    public long getKills() {
        long amount = getStats(leader).getKills();
        for (UUID uuid : members) {
            amount += getStats(uuid).getKills();
        }
        return amount;
    }

    public long getDeaths() {
        long amount = getStats(leader).getDeaths();
        for (UUID uuid : members) {
            amount += getStats(uuid).getDeaths();
        }
        return amount;
    }

    public long getXp() {
        long amount = getStats(leader).getXp();
        for (UUID uuid : members) {
            amount += getStats(uuid).getXp();
        }
        return amount;
    }

    public void saveAsync() {
        PvPClans.getInstance().database.setClan(id, name, prefix, leader, joinModerators(), joinMembers(), level, maxMembers, joinPerks(), timeStamp);
    }
}