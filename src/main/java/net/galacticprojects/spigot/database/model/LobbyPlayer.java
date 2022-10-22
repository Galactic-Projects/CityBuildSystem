package net.galacticprojects.spigot.database.model;

import java.util.UUID;

public class LobbyPlayer {

    private final UUID uuid;
    private String ip;
    private long onlineTime;

    private int coins;
    private String language;
    private int level;

    public LobbyPlayer (UUID uuid, String ip, long onlineTime, int coins, String language, int level) {
        this.uuid = uuid;
        this.ip = ip;
        this.onlineTime = onlineTime;
        this.coins = coins;
        this.language = language;
        this.level = level;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getIP() {
        return ip;
    }

    public long getOnlineTime() { return onlineTime; }


    public int getCoins() {
        return coins;
    }

    public String getLanguage() {return language;}
    public int getLevel() {return level;}

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
