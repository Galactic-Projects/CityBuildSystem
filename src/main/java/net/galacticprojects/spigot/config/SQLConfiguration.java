package net.galacticprojects.spigot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import me.lauriichan.laylib.logger.ISimpleLogger;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.SQLTable;
import net.galacticprojects.spigot.util.Ref;

import java.io.File;


public final class SQLConfiguration extends BaseConfiguration {

    private final Ref<SQLDatabase> database = Ref.of();

    public SQLConfiguration(ISimpleLogger logger, File dataFolder) {
        super(logger, dataFolder, "mysql.json");
    }

    @Override
    protected void onLoad() throws Throwable {
        if(database.isPresent()){
            SQLDatabase db = database.get();
            database.set(null);
            db.shutdown();
        }
        for(SQLTable table : SQLTable.values()){
            table.tableName(config.getValueOrDefault("table." + table.name(), table.defaultTableName()));
        }
        int port = config.getValueOrDefault("database.port", 3306).intValue();
        String host = config.getValueOrDefault("database.host", "localhost");
        String database = config.getValueOrDefault("database.datenbank", "root");
        String username = config.getValueOrDefault("database.username", "root");
        String password = config.getValueOrDefault("database.password", "password");
        try {
            SQLDatabase db = new SQLDatabase(logger, () -> {
                HikariConfig poolConfig = new HikariConfig();
                poolConfig.setConnectionTimeout(7500);
                poolConfig.setMaximumPoolSize(8);
                poolConfig.setMinimumIdle(1);
                poolConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database
                        + "?serverTimezone=UTC&useLegacyDatetimeCode=false");
                poolConfig.setUsername(username);
                poolConfig.setPassword(password);
                return new HikariPool(poolConfig);
            });
            this.database.set(db);
        } catch(IllegalStateException | NullPointerException exp) {
            logger.warning(exp);
            return;
        }
    }

    public Ref<SQLDatabase> getDatabaseRef() {
        return database;
    }
}
