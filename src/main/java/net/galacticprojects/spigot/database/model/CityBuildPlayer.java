package net.galacticprojects.spigot.database.model;

import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import net.galacticprojects.spigot.database.SQLDatabase;

import java.util.UUID;

public class CityBuildPlayer {


    UUID uniqueId;
    int money;

    SQLDatabase database;

    public CityBuildPlayer(UUID uniqueId, int money, SQLDatabase database) {
        this.money = money;
        this.uniqueId = uniqueId;
        this.database = database;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getMoney() {
        return money;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
