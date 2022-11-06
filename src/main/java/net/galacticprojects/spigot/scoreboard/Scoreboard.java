package net.galacticprojects.spigot.scoreboard;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.galacticprojects.spigot.CityBuild;
import net.galacticprojects.spigot.boost.GalacticBoost;
import net.galacticprojects.spigot.database.LinkedList;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.database.model.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class Scoreboard extends ScoreboardBuilder{

    public Scoreboard(Player player, CityBuild system) {
        super(player, system, "§8「 §5§lGALACTIC§d§lPROJECTS§8」");
    }
    @Override
    public void createScoreboard() {

        SQLDatabase database = system.getSqlConfiguration().getDatabaseRef().get();

        LobbyPlayer lobbyPlayer = database.getLobbyPlayer(player.getUniqueId()).join();

        IPermissionGroup info = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId()));

        IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

        CityBuildPlayer cityPlayer = database.getPlayer(player.getUniqueId()).join();

        long time = lobbyPlayer.getOnlineTime();
        long minute = (time % 3600) / 60;
        long hours = (time % 86400) / 3600;
        long days = (time % 2629746) / 86400;
        String onlineTime = days + " d " + hours + " h " + minute + " m";
        setScore("§a§3§4§a", 14);
        setScore("§8▏ §2§lPROFILE", 13);
        setScore("§a" + player.getName(), 12);
        setScore("§a§3§4§a", 11);
        setScore("§8▏ §a§lMONEY", 10);
        setScore("§2" + formatValue(cityPlayer.getMoney()), 9);
        setScore("§r§1§5", 8);
        setScore("§8▏ §5§lBOOSTS", 7);
        setScore("§d" + formatValue(lobbyPlayer.getCoins()), 6);
        setScore("§r§1§5", 5);
        setScore("§8▏ §6§lCITYBUILD", 4);
        setScore("§e" + manager.getOnlinePlayer(lobbyPlayer.getUUID()).getConnectedService().getServerName(), 3);
        setScore("§r§1§5", 2);
        setScore("§8▏ §3§lONLINE", 1);
        setScore("§b" + onlineTime, 0);
    }

    @Override
    public void update() {

    }

    int id = 1;

    private void run() {

        new BukkitRunnable() {

            @Override
            public void run() {
                SQLDatabase database = system.getSqlConfiguration().getDatabaseRef().get();

                LobbyPlayer lobbyPlayer = database.getLobbyPlayer(player.getUniqueId()).join();
                CityBuildPlayer cityPlayer = database.getPlayer(player.getUniqueId()).join();

                        IPermissionGroup info = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId()));

                        IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

                        LinkedList<GalacticBoost> boostLinkedList = new LinkedList<>(cityPlayer.getBoostId(), new GalacticBoost(null, cityPlayer, null, null), database);

                        long time = lobbyPlayer.getOnlineTime();
                        long minute = (time % 3600) / 60;
                        long hours = (time % 86400) / 3600;
                        long days = (time % 2629746) / 86400;
                        String onlineTime = days + " d " + hours + " h " + minute + " m";
                        setScore("§a§3§4§a", 14);
                        setScore("§8▏ §2§lPROFILE", 13);
                        setScore("§a" + player.getName(), 12);
                        setScore("§a§3§4§a", 11);
                        setScore("§8▏ §a§lMONEY", 10);
                        setScore("§2" + formatValue(cityPlayer.getMoney()), 9);
                        setScore("§r§1§5", 8);
                        if(boostLinkedList.getDynamicArray().length() == 1) {
                            setScore("§8▏ §5§lBOOSTS", 7);
                            setScore("§d No Boosts", 6);
                        }
                        if(boostLinkedList.getDynamicArray().length() == id || boostLinkedList.getDynamicArray().length() > id ) {
                            setScore("§8▏ §5§lBOOSTS", 7);
                            setScore("§d" + boostLinkedList.getDynamicArray().get(id), 6);
                        }
                        if(boostLinkedList.getDynamicArray().length() < id) {
                            id = 1;
                            setScore("§8▏ §5§lBOOSTS", 7);
                            setScore("§d" + boostLinkedList.getDynamicArray().get(id), 6);
                        }
                        setScore("§r§1§5", 5);
                        setScore("§8▏ §6§lCITYBUILD", 4);
                        setScore("§e" + manager.getOnlinePlayer(lobbyPlayer.getUUID()).getConnectedService().getServerName(), 3);
                        setScore("§r§1§5", 2);
                        setScore("§8▏ §3§lONLINE", 1);
                        setScore("§b" + onlineTime, 0);
                        id++;

                        if(id == 5) {
                            id = 1;
                        }

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
