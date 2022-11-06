package net.galacticprojects.spigot.boost;

import net.galacticprojects.spigot.database.LinkedList;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GalacticBoost {

    private int time;
    private int level;
    private PotionEffectType type;
    private String typeName;
    private LinkedList<GalacticBoost> linkedList;

    private Player player;

    public GalacticBoost(Player player, CityBuildPlayer cityBuildPlayer, GalacticBoostType type, SQLDatabase database) {
        linkedList = new LinkedList<>(cityBuildPlayer.getBoostId(), this, database);
        if(linkedList.getDynamicArray().length() == 6) {
            return;
        }
        this.player = player;
        this.time = type.count;
        this.level = type.level;
        for(PotionEffectType type1 : PotionEffectType.values()) {
            if(type.name().replaceAll("_", "").replaceAll("I", "").equals(type1.getName().replaceAll("_", ""))) {
                this.type = type1;
            }
        }
        if(this.type == null) {
            this.typeName = type.name().replaceAll("_", "").replaceAll("I", "").toLowerCase();
        }
        linkedList.add(this);
    }

    public GalacticBoost(UUID player, CityBuildPlayer cityBuildPlayer, int time, int level, PotionEffectType type, SQLDatabase database) {
        linkedList = new LinkedList<>(cityBuildPlayer.getBoostId(), this, database);
        if(linkedList.getDynamicArray().length() == 6) {
            return;
        }
        this.player = Bukkit.getPlayer(player);
        this.time = time;
        this.level = level;
        this.type = type;
        linkedList.add(this);
    }

    public GalacticBoost(UUID player, CityBuildPlayer cityBuildPlayer, int time, int level, String typeName, SQLDatabase database) {
        linkedList = new LinkedList<>(cityBuildPlayer.getBoostId(), this, database);
        if(linkedList.getDynamicArray().length() == 6) {
            return;
        }
        this.player = Bukkit.getPlayer(player);
        this.time = time;
        this.level = level;
        this.typeName = typeName;
        linkedList.add(this);
    }

    public int getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTime() {
        return time;
    }

    public String getTypeName() {
        return typeName;
    }

    public PotionEffectType getType() {
        return type;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setType(PotionEffectType type) {
        this.type = type;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
