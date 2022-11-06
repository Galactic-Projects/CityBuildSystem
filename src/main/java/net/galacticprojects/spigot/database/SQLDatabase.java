package net.galacticprojects.spigot.database;

import com.comphenix.protocol.PacketType;
import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.spigot.boost.GalacticBoost;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.database.model.LobbyPlayer;
import net.galacticprojects.spigot.database.model.Sync;
import net.galacticprojects.spigot.util.Ref;
import net.galacticprojects.spigot.util.cache.Cache;
import net.galacticprojects.spigot.util.cache.ThreadSafeCache;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLDatabase {


    @SuppressWarnings("rawtypes")
    private static final class SQLTimer extends TimerTask {
        private final List<Cache<?, ?>> caches;

        public SQLTimer(final List<Cache<?, ?>> caches) {
            this.caches = caches;
        }

        @Override
        public void run() {
            for (final Cache cache : caches) {
                cache.tick();
            }
        }
    }

    private <A, B> ThreadSafeCache<A, B> newCache(Class<A> clazz, int cacheTime){
        if(caches.isLocked()) {
            return null;
        }
        ThreadSafeCache<A, B> cache = new ThreadSafeCache<>(clazz, cacheTime);
        caches.get().add(cache);
        return cache;
    }

    public static final String SELECT_PLAYER = String.format("SELECT * FROM `%s` WHERE uuid = ?", SQLTable.CityBuild);
    public static final String INSERT_PLAYER = String
            .format("INSERT INTO `%s` (uuid, Money, BoostId) VALUES (?, ?, ?)", SQLTable.CityBuild);
    public static final String UPDATE_PLAYER = String.format("UPDATE `%s` SET Money = ? WHERE uuid = ?", SQLTable.CityBuild);

    public static final String SELECT_LOBBY_PLAYER = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.PLAYER_TABLE);
    public static final String UPDATE_LOBBY_PLAYER = String.format("UPDATE `%s` SET IP = ?, COINS = ?, LEVEL = ?, LANGUAGE = ?, ONLINETIME = ? WHERE UUID = ?", SQLTable.PLAYER_TABLE);

    public static final String INSERT_BOOST = String.format("INSERT INTO `%s` (id, boostId, uuid, level, count, type) VALUES (?, ?, ?, ?, ?, ?);", SQLTable.BOOST_TABLE);
    public static final String SELECT_BOOST = String.format("SELECT * FROM `%s` WHERE id = ?", SQLTable.BOOST_TABLE);
    public static final String UPDATE_BOOST = String.format("UPDATE `%s` SET time = ?, type = ?", SQLTable.BOOST_TABLE);

    public static final String INSERT_SYNC = String.format("INSERT INTO `%s` (UUID, INVENTORY, ARMOR, XP, LEVEL, LAST_SERVICE, SYNC_COMPLETE) VALUES (?, ?, ?, ?, ?, ?, ?)", SQLTable.SYNC_TABLE);
    public static final String SELECT_SYNC = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.SYNC_TABLE);
    public static final String UPDATE_SYNC = String.format("UPDATE `%s` SET INVENTORY = ?, ARMOR = ?, XP = ?, LEVEL = ?, LAST_SERVICE = ?, SYNC_COMPLETE = ? WHERE UUID = ?", SQLTable.SYNC_TABLE);
    public final HikariPool pool;

    private final Timer timer;

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());
    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Cache<UUID, CityBuildPlayer> playerCache = newCache(UUID.class, 300);
    private final Cache<UUID, LobbyPlayer> lobbyPlayerCache = newCache(UUID.class, 300);
    private final ISimpleLogger logger;

    public SQLDatabase(final ISimpleLogger logger, final IPoolProvider provider) {
        caches.get();
        this.logger = logger;
        this.pool = Objects.requireNonNull(provider, "Provider can't be null").createPool();
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new SQLTimer(caches.set(Collections.unmodifiableList(caches.get())).lock().get()), 0, 1000);
        try (Connection connection = pool.getConnection(15000)) {
            final PreparedStatement statement = connection.prepareStatement("/* ping */ SELECT 1");
            statement.setQueryTimeout(15);
            statement.executeQuery();
            createTables(connection);
        } catch (final SQLException exp) {
            shutdown();
            throw new IllegalStateException("MySQL connection test failed", exp);
        }
    }

    public void createTables(Connection connection) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + SQLTable.CityBuild + " (uuid VARCHAR(36), Money int(20), BoostId int(100), PRIMARY KEY(uuid));");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + SQLTable.SYNC_TABLE + " (id int(10) AUTO_INCREMENT, UUID VARCHAR(36) NOT NULL UNIQUE, INVENTORY LONGTEXT NOT NULL, ARMOR LONGTEXT NOT NULL, XP VARCHAR(20), LEVEL VARCHAR(8), LAST_SERVICE VARCHAR(22), SYNC_COMPLETE varchar(5) NOT NULL, PRIMARY KEY(id));");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + SQLTable.BOOST_TABLE + " (id int(100), boostId int(100), uuid VARCHAR(36), level int(10), count int(200), type VARCHAR(50), PRIMARY KEY(id));");
            statement.executeUpdate();
    }


    public void shutdown() {
        timer.cancel();
        try {
            pool.shutdown();
        } catch (final Throwable e) {
            // Ignore
        }
    }

    public CompletableFuture<CityBuildPlayer> createPlayer(UUID uniqueId, int boostId) {

        return CompletableFuture.supplyAsync(() -> {

            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.setInt(2, 10000);
                statement.setString(3, "");
                CityBuildPlayer player = new CityBuildPlayer(uniqueId, 10000, boostId, this);
                return player;
            }catch (SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<CityBuildPlayer> getPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    int coins = set.getInt("Money");
                    int boostId = set.getInt("BoostId");
                    CityBuildPlayer player = new CityBuildPlayer(uniqueId, coins, boostId, this);
                    return player;
                }
                return null;
            }catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<CityBuildPlayer> updatePlayer(CityBuildPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_PLAYER);
                statement.setInt(1, player.getMoney());
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
                return player;
            }catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                return null;
            }
        }, service);
    }
//


    public CompletableFuture<LobbyPlayer> getLobbyPlayer(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_LOBBY_PLAYER);
                statement.setString(1, uniqueId.toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    String ip = set.getString("IP");
                    int coins = set.getInt("COINS");
                    int level = set.getInt("LEVEL");
                    String language = set.getString("LANGUAGE");
                    long onlineTime = set.getLong("ONLINETIME");
                    LobbyPlayer player = new LobbyPlayer(uniqueId, ip, onlineTime, coins, language, level);
                    return player;
                }
                return null;
            }catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<LobbyPlayer> updateLobbyPlayer(LobbyPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_LOBBY_PLAYER);
                statement.setString(1, player.getIP());
                statement.setInt(2, player.getCoins());
                statement.setInt(3, player.getLevel());
                statement.setString(4, player.getLanguage());
                statement.setLong(5, player.getOnlineTime());
                statement.setString(6, player.getUUID().toString());
                statement.executeUpdate();
                lobbyPlayerCache.put(player.getUUID(), player);
                return player;
            }catch (SQLException e) {
                logger.warning("A few SQL things went wrong while delete Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Sync> createSync(Player player, String last_service) {
        return CompletableFuture.supplyAsync(() -> {
            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_SYNC);
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, "none");
                statement.setString(3, "none");
                statement.setDouble(4, 0.0);
                statement.setInt(5, 0);
                statement.setString(6, last_service);
                statement.setBoolean(7, false);
                statement.executeUpdate();
                return new Sync(player.getUniqueId(), "none", "none", 0.0, 0, last_service, false, this);
            } catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Sync> getSync(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_SYNC);
                statement.setString(1, player.getUniqueId().toString());
                ResultSet set = statement.executeQuery();
                if(set.next()) {
                    String inventory = set.getString("INVENTORY");
                    String armor = set.getString("ARMOR");
                    double xp = set.getDouble("XP");
                    int level = set.getInt("LEVEL");
                    String service = set.getString("LAST_SERVICE");
                    boolean sync = set.getBoolean("SYNC_COMPLETE");
                    return new Sync(player.getUniqueId(), inventory, armor, xp, level, service, sync, this);
                }
                return null;
            } catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<Sync> updateSync(Sync sync, String last_service) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_SYNC);
                statement.setString(1, sync.getInventory());
                statement.setString(2, sync.getArmor());
                statement.setDouble(3, sync.getXp());
                statement.setInt(4, sync.getLevel());
                statement.setString(5, last_service);
                statement.setBoolean(6, sync.isSync());
                statement.setString(7, sync.getUniqueId().toString());
                statement.executeUpdate();
                return sync;
            } catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<GalacticBoost> insertBoost(GalacticBoost boost, CityBuildPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
           try(Connection connection = pool.getConnection()) {

               PreparedStatement statement = connection.prepareStatement(INSERT_BOOST);
               statement.setInt();

           }catch (SQLException e) {
               logger.warning("A few SQL things went wrong while get Player", e);
               return null;
           }
        });
    }

}
