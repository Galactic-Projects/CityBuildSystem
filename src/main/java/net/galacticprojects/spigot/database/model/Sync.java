package net.galacticprojects.spigot.database.model;

import net.galacticprojects.spigot.database.SQLDatabase;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class Sync {

    UUID uniqueId;
    String inventory;
    String armor;
    double xp;
    int level;
    String last_service;
    boolean sync;

    SQLDatabase database;

    public Sync(UUID uniqueId, String inventory, String armor, double xp, int level, String last_service, boolean sync, SQLDatabase database) {
        this.uniqueId = uniqueId;
        this.inventory = inventory;
        this.armor = armor;
        this.xp = xp;
        this.level = level;
        this.last_service = last_service;
        this.sync = sync;
        this.database = database;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getArmor() {
        return armor;
    }

    public String getInventory() {
        return inventory;
    }

    public double getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public String getLast_service() {
        return last_service;
    }

    public boolean isSync() {
        return sync;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
        database.updateSync(this, last_service);
    }

    public void setLevel(int level) {
        this.level = level;
        database.updateSync(this, last_service);
    }

    public void setXp(double xp) {
        this.xp = xp;
        database.updateSync(this, last_service);
    }

    public void setLast_service(String last_service) {
        this.last_service = last_service;
        database.updateSync(this, last_service);
    }

    public void setSync(boolean sync) {
        this.sync = sync;
        database.updateSync(this, last_service);
    }

    public void update(String armor, String inventory, double xp, int level, String last_service, boolean sync) {
        this.armor = armor;
        this.inventory = inventory;
        this.xp = xp;
        this.level = level;
        this.last_service = last_service;
        this.sync = sync;
        database.updateSync(this, last_service);
    }

}
