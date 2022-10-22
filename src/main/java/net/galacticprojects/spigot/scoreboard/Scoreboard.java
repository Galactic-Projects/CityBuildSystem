package net.galacticprojects.spigot.scoreboard;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import net.galacticprojects.lobbysystem.LobbySystem;
import net.galacticprojects.lobbysystem.config.ScoreboardConfiguration;
import net.galacticprojects.lobbysystem.database.SQLDatabase;
import net.galacticprojects.lobbysystem.database.model.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class Scoreboard extends ScoreboardBuilder{

    public Scoreboard(Player player, LobbySystem system) {
        super(player, system, "§8「 §5§lGALACTIC§d§lPROJECTS§8」");
        run();
    }
    @Override
    public void createScoreboard() {
        ScoreboardConfiguration scoreboardConfiguration = system.getScoreboardConfiguration();

        SQLDatabase database = system.getSqlConfiguration().getDatabaseRef().get();

        LobbyPlayer lobbyPlayer = database.getPlayer(player.getUniqueId()).join();

        IPermissionGroup info = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId()));

        long time = lobbyPlayer.getOnlineTime();
        long minute = (time % 3600) / 60;
        long hours = (time % 86400) / 3600;
        long days = (time % 2629746) / 86400;

        String onlineTime = days + " d " + hours + " h " + minute + " m";
        int test = scoreboardConfiguration.getScoreboard().size();
        setScore("§a§3§4§a", 14);
        setScore("§8▏ §2§lPROFILE            &r", 13);
        setScore("§a" + player.getName(), 12);
        setScore("§a§3§4§a", 11);
        setScore("§8▏ §9§lRANK", 10);
        setScore("§1" + info.getPrefix().replace(" ", "").replace("§7§l▪", ""), 9);
        setScore("§r§1§5", 8);
        setScore("§8▏ §6§lGALACTIC CREDITS", 7);
        setScore("§e" + formatValue(lobbyPlayer.getCoins()), 6);
        setScore("§r§1§5", 5);
        setScore("§8▏ §5§lLEVEL", 4);
        setScore("§d" + lobbyPlayer.getLevel(), 3);
        setScore("§r§1§5", 2);
        setScore("§8▏ §3§lONLINE", 1);
        setScore("§b" + onlineTime, 0);
    }

    @Override
    public void update() {

    }

    private void run() {

        new BukkitRunnable() {

            @Override
            public void run() {
                ScoreboardConfiguration scoreboardConfiguration = system.getScoreboardConfiguration();

                SQLDatabase database = system.getSqlConfiguration().getDatabaseRef().get();

                database.getPlayerWithoutCache(player.getUniqueId()).thenAccept(lobbyPlayer -> {
                    IPermissionGroup info = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId()));

                    long time = lobbyPlayer.getOnlineTime();
                    long minute = (time % 3600) / 60;
                    long hours = (time % 86400) / 3600;
                    long days = (time % 2629746) / 86400;
                    String onlineTime = days + " d " + hours + " h " + minute + " m";
                    int test = scoreboardConfiguration.getScoreboard().size();
                    setScore("§a§3§4§a", 14);
                    setScore("§8▏ §2§lPROFILE", 13);
                    setScore("§a" + player.getName(), 12);
                    setScore("§a§3§4§a", 11);
                    setScore("§8▏ §9§lRANK", 10);
                    setScore("§1" + info.getPrefix(), 9);
                    setScore("§r§1§5", 8);
                    setScore("§8▏ §6§lGALACTIC CREDITS", 7);
                    setScore("§e" + formatValue(lobbyPlayer.getCoins()), 6);
                    setScore("§r§1§5", 5);
                    setScore("§8▏ §5§lLEVEL", 4);
                    setScore("§d" + lobbyPlayer.getLevel(), 3);
                    setScore("§r§1§5", 2);
                    setScore("§8▏ §3§lONLINE", 1);
                    setScore("§b" + onlineTime, 0);
                });

            }
        }.runTaskTimer(system, 1, 40);
    }

    private String formatValue(int value) {
        String format = Integer.toString(value);
        DecimalFormat decimal = new DecimalFormat();
        format = decimal.format((double) Integer.parseInt(format));
        return format;
    }
}
