package net.galacticprojects.spigot.database.model;

import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import net.galacticprojects.spigot.boost.GalacticBoost;
import net.galacticprojects.spigot.database.SQLDatabase;

import java.util.UUID;

public class CityBuildPlayer {


    UUID uniqueId;
    int money;
    int boostId;
    SQLDatabase database;

    public CityBuildPlayer(UUID uniqueId, int money, int boostId, SQLDatabase database) {
        this.money = money;
        this.uniqueId = uniqueId;
        this.boostId = boostId;
        this.database = database;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getMoney() {
        return money;
    }

    public int getBoostId() {
        return boostId;
    }

    public void setMoney(int money) {
        this.money = money;
        database.updatePlayer(this);
    }

}
