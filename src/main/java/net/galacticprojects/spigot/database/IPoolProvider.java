package net.galacticprojects.spigot.database;

import com.zaxxer.hikari.pool.HikariPool;

public interface IPoolProvider {

    HikariPool createPool();

}
