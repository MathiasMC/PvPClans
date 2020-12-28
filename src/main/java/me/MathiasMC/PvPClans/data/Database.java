package me.MathiasMC.PvPClans.data;

import me.MathiasMC.PvPClans.PvPClans;
import me.MathiasMC.PvPClans.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Database {

    private final PvPClans plugin;

    private Connection connection;

    public Database(PvPClans plugin) {
        this.plugin = plugin;
        (new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.createStatement().execute("SELECT 1");
                    }
                } catch (SQLException e) {
                    connection = get();
                }
            }
        }).runTaskTimerAsynchronously(plugin, 60 * 20, 60 * 20);
    }

    private Connection get() {
        try {
            if (plugin.getFileUtils().config.getBoolean("mysql.use", false)) {
                Utils.info("[Database] ( Connected ) ( MySQL )");
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection("jdbc:mysql://" +
                        plugin.getFileUtils().config.getString("mysql.host") + ":" +
                        plugin.getFileUtils().config.getString("mysql.port") + "/" +
                        plugin.getFileUtils().config.getString("mysql.database"),
                        plugin.getFileUtils().config.getString("mysql.username"),
                        plugin.getFileUtils().config.getString("mysql.password"));
            } else {
                Utils.info("[Database] ( Connected ) ( SQLite )");
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection("jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            Utils.exception(e.getStackTrace(), e.getMessage());
            return null;
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private boolean check() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = get();
            if (connection == null || connection.isClosed()) {
                return false;
            }
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `players` (`uuid` char(36) PRIMARY KEY, `clan` varchar(200), `coins` bigint(255));");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `clans` (`clan` varchar(200) PRIMARY KEY, `name` varchar(200), `prefix` varchar(200), `leader` char(36), `moderators` varchar(2000), `members` varchar(2000), `level` bigint(255), `maxMembers` bigint(255), `perks` varchar(20000), `lastseen` DATETIME);");
        }
        return true;
    }

    public boolean set() {
        try {
            return check();
        } catch (SQLException e) {
            Utils.exception(e.getStackTrace(), e.getMessage());
            return false;
        }
    }

    public void insertClanPlayer(UUID playerUUID) {
        if (!set()) return;
        final String uuid = playerUUID.toString();
            BukkitRunnable r = new BukkitRunnable() {
                @Override
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM players WHERE uuid= '" + uuid + "';");
                        if (!resultSet.next()) {
                            preparedStatement = connection.prepareStatement("INSERT INTO players (uuid, clan, coins) VALUES(?, ?, ?);");
                            preparedStatement.setString(1, uuid);
                            preparedStatement.setString(2, "");
                            preparedStatement.setLong(3, 0);
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public long[] getClanPlayer(final UUID playerUUID) {
        if (set()) {
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM players WHERE uuid= '" + playerUUID.toString() + "';");
                if (resultSet.next()) {
                    return new long[] { resultSet.getLong("clan"), resultSet.getLong("coins") };
                }
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            } finally {
                closeStatements(resultSet, statement);
            }
        }
        return new long[] {0, 0};
    }

    public void setClanPlayer(UUID playerUUID, long clanID, long coins) {
        if (!set()) return;
        String uuid = playerUUID.toString();
            BukkitRunnable r = new BukkitRunnable() {
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM players WHERE uuid= '" + uuid + "';");
                        if (resultSet.next()) {
                            preparedStatement = connection.prepareStatement("UPDATE players SET clan = ?, coins = ? WHERE uuid = ?");
                            preparedStatement.setLong(1, clanID);
                            preparedStatement.setLong(2, coins);
                            preparedStatement.setString(3, uuid);
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public void insertClan(UUID playerUUID, long clanID, String clan) {
        if (!set()) return;
            BukkitRunnable r = new BukkitRunnable() {
                @Override
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM clans WHERE clan= '" + clanID + "';");
                        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `" + clanID + "` (`uuid` char(36) PRIMARY KEY, `coins` bigint(255), `share` tinyint, `xp` bigint(255), `kills` bigint(255), `deaths` bigint(255));");
                        if (!resultSet.next()) {
                            preparedStatement = connection.prepareStatement("INSERT INTO clans (clan, name, prefix, leader, moderators, members, level, maxMembers, perks, lastseen) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                            preparedStatement.setLong(1, clanID);
                            preparedStatement.setString(2, clan);
                            preparedStatement.setString(3, clan);
                            preparedStatement.setString(4, playerUUID.toString());
                            preparedStatement.setString(5, "");
                            preparedStatement.setString(6, "");
                            preparedStatement.setLong(7, plugin.getFileUtils().config.getLong("default.level", 1));
                            preparedStatement.setLong(8, plugin.getFileUtils().config.getLong("default.members", 1));
                            preparedStatement.setString(9, "");
                            preparedStatement.setTimestamp(10, new Timestamp(new Date().getTime()));
                            preparedStatement.executeUpdate();
                            plugin.getClan(clanID);
                            plugin.clanNames.add(clan);
                            ClanPlayer clanPlayer = plugin.getClanPlayer(playerUUID);
                            clanPlayer.setClanID(clanID);
                            clanPlayer.saveAsync();
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public String[] getClan(final long id) {
        if (set()) {
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM clans WHERE clan= '" + id + "';");
                if (resultSet.next()) {
                    return new String[]{
                            resultSet.getString("name"),
                            resultSet.getString("prefix"),
                            resultSet.getString("leader"),
                            resultSet.getString("moderators"),
                            resultSet.getString("members"),
                            String.valueOf(resultSet.getLong("level")),
                            String.valueOf(resultSet.getLong("maxMembers")),
                            resultSet.getString("perks"),
                            String.valueOf(resultSet.getTimestamp("lastseen"))};
                }
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            } finally {
                closeStatements(resultSet, statement);
            }
        }
        return new String[] { "0", "", "", "", "", String.valueOf(plugin.getFileUtils().config.getLong("default.level", 1)), String.valueOf(plugin.getFileUtils().config.getLong("default.members", 1)), "", String.valueOf(new Timestamp(new Date().getTime())) };
    }

    public void setClan(long clanID, String name, String prefix, UUID leader, String moderators, String members, long level, long maxMembers, String perks, Timestamp timestamp) {
        if (!set()) return;
            BukkitRunnable r = new BukkitRunnable() {
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM clans WHERE clan= '" + clanID + "';");
                        if (resultSet.next()) {
                            preparedStatement = connection.prepareStatement("UPDATE clans SET clan = ?, name = ?, prefix = ?, leader = ?, moderators = ?, members = ?, level = ?, maxMembers = ?, perks = ?, lastseen = ? WHERE clan = ?");
                            preparedStatement.setLong(1, clanID);
                            preparedStatement.setString(2, name);
                            preparedStatement.setString(3, prefix);
                            preparedStatement.setString(4, leader.toString());
                            preparedStatement.setString(5, moderators);
                            preparedStatement.setString(6, members);
                            preparedStatement.setLong(7, level);
                            preparedStatement.setLong(8, maxMembers);
                            preparedStatement.setString(9, perks);
                            preparedStatement.setTimestamp(10, timestamp);
                            preparedStatement.setLong(11, clanID);
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public ArrayList<Long> getClans() {
        ArrayList<Long> array = new ArrayList<>();
        if (set()) {
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM clans");
                while (resultSet.next()) {
                    array.add(resultSet.getLong("clan"));
                }
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            } finally {
                closeStatements(resultSet, statement);
            }
            return array;
        }
        return array;
    }

    public void deleteClan(final Clan clan) {
        if (!set()) return;
            final long clanID = clan.getID();
            BukkitRunnable r = new BukkitRunnable() {
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        connection.createStatement().executeUpdate("DELETE FROM '" + clanID + "'");
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM clans WHERE clan= '" + clanID + "';");
                        if (resultSet.next()) {
                            preparedStatement = connection.prepareStatement("DELETE FROM clans WHERE clan = ?");
                            preparedStatement.setLong(1, clanID);
                            preparedStatement.executeUpdate();
                            ClanPlayer clanLeader = plugin.getClanPlayer(clan.getLeader());
                            clanLeader.setClanID(0);
                            clanLeader.saveAsync();
                            plugin.getSaveTask().getStats().remove(clanLeader.getUniqueId());
                            for (UUID uuid : clan.getMembers()) {
                                final ClanPlayer clanPlayer = plugin.getClanPlayer(uuid);
                                clanPlayer.setClanID(0);
                                clanPlayer.saveAsync();
                                plugin.getSaveTask().getStats().remove(uuid);
                            }
                            plugin.getSaveTask().getClans().remove(clanID);
                            plugin.clanNames.remove(clan.getName());
                            plugin.getStatsManager().updateTopMap();
                            plugin.removeClan(clanID);
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public void setClanStats(final UUID playerUUID, final long clanID, final long coins, final long share, final long xp, final long kills, final long deaths) {
        if (!set()) return;
        final String uuid = playerUUID.toString();
            BukkitRunnable r = new BukkitRunnable() {
                public void run() {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        resultSet = connection.createStatement().executeQuery("SELECT * FROM '" + clanID + "' WHERE uuid= '" + uuid + "';");
                        if (resultSet.next()) {
                            preparedStatement = connection.prepareStatement("UPDATE '" + clanID + "' SET coins = ?, share = ?, xp = ?, kills = ?, deaths = ? WHERE uuid = ?");
                            preparedStatement.setLong(1, coins);
                            preparedStatement.setLong(2, share);
                            preparedStatement.setLong(3, xp);
                            preparedStatement.setLong(4, kills);
                            preparedStatement.setLong(5, deaths);
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException exception) {
                        Utils.exception(exception.getStackTrace(), exception.getMessage());
                    } finally {
                        closeStatements(resultSet, preparedStatement);
                    }
                }
            };
            r.runTaskAsynchronously(plugin);
    }

    public long[] getClanStats(final UUID playerUUID, final long clanID) {
        if (set()) {
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM '" + clanID + "' WHERE uuid= '" + playerUUID.toString() + "';");
                if (resultSet.next()) {
                    return new long[]{
                            resultSet.getLong("coins"),
                            resultSet.getInt("share"),
                            resultSet.getLong("xp"),
                            resultSet.getLong("kills"),
                            resultSet.getLong("deaths")};
                }
                BukkitRunnable r = new BukkitRunnable() {
                    @Override
                    public void run() {
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;
                        try {
                            resultSet = connection.createStatement().executeQuery("SELECT * FROM '" + clanID + "' WHERE uuid= '" + playerUUID.toString() + "';");
                            if (!resultSet.next()) {
                                preparedStatement = connection.prepareStatement("INSERT INTO '" + clanID + "' (uuid, coins, share, xp, kills, deaths) VALUES(?, ?, ?, ?, ?, ?);");
                                preparedStatement.setString(1, playerUUID.toString());
                                preparedStatement.setLong(2, plugin.getFileUtils().config.getLong("default.coins", 0));
                                if (plugin.getFileUtils().config.getBoolean("default.share", false)) {
                                    preparedStatement.setInt(3, 1);
                                } else {
                                    preparedStatement.setInt(3, 0);
                                }
                                preparedStatement.setLong(4, 0);
                                preparedStatement.setLong(5, 0);
                                preparedStatement.setLong(6, 0);
                                preparedStatement.executeUpdate();
                            }
                        } catch (SQLException exception) {
                            Utils.exception(exception.getStackTrace(), exception.getMessage());
                        } finally {
                            closeStatements(resultSet, preparedStatement);
                        }
                    }
                };
                r.runTaskAsynchronously(plugin);
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            } finally {
                closeStatements(resultSet, statement);
            }
        }
        return new long[] { 0, 0, 0, 0, 0 };
    }

    private void closeStatements(ResultSet resultSet, PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            }
        }
    }
    private void closeStatements(ResultSet resultSet, Statement statement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException exception) {
                Utils.exception(exception.getStackTrace(), exception.getMessage());
            }
        }
    }
}
