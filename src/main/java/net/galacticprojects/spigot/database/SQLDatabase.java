package net.galacticprojects.spigot.database;

import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.util.Ref;
import net.galacticprojects.spigot.util.cache.Cache;
import net.galacticprojects.spigot.util.cache.ThreadSafeCache;

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

    public static final String SELECT_PLAYER = String.format("SELECT * FROM `%s` WHERE UUID = ?", SQLTable.CityBuild);
    public static final String INSERT_PLAYER = String
            .format("INSERT INTO `%s` (UUID, MONEY) VALUES (?, ?)", SQLTable.CityBuild);
    public static final String UPDATE_PLAYER = String.format("UPDATE `%s` SET MONEY = ? WHERE UUID = ?", SQLTable.CityBuild);

    private final HikariPool pool;

    private final Timer timer;

    private final Ref<List<Cache<?, ?>>> caches = Ref.of(new ArrayList<>());
    private final ExecutorService service = Executors.newCachedThreadPool();

    private final Cache<UUID, CityBuildPlayer> playerCache = newCache(UUID.class, 300);
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
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Lobby(UUID VARCHAR(36), LOTTERY VARCHAR(6), CHEST VARCHAR(6), DATA VARCHAR(96))");
            preparedStatement.executeUpdate();
        } catch (final SQLException exp) {
            shutdown();
            throw new IllegalStateException("MySQL connection test failed", exp);
        }
    }

    public void shutdown() {
        timer.cancel();
        try {
            pool.shutdown();
        } catch (final Throwable e) {
            // Ignore
        }
    }

    public CompletableFuture<CityBuildPlayer> createPlayer(UUID uniqueId) {

        return CompletableFuture.supplyAsync(() -> {

            try(Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER);
                statement.setString(1, uniqueId.toString());
                statement.setInt(2, 10000);
                CityBuildPlayer player = new CityBuildPlayer(uniqueId, 10000, this);
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
                    int coins = set.getInt("MONEY");
                    CityBuildPlayer player = new CityBuildPlayer(uniqueId, coins, this);
                    return player;
                }
                return null;
            }catch(SQLException e) {
                logger.warning("A few SQL things went wrong while get Player", e);
                return null;
            }
        }, service);
    }

    public CompletableFuture<CityBuildPlayer> updatePlayer(CityBuildPlayer  player) {
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

}
