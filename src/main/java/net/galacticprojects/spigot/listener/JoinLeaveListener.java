package net.galacticprojects.spigot.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.galacticprojects.spigot.CityBuild;
import net.galacticprojects.spigot.boost.GalacticBoost;
import net.galacticprojects.spigot.boost.GalacticBoostType;
import net.galacticprojects.spigot.database.LinkedList;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.database.model.Sync;
import net.galacticprojects.spigot.scoreboard.Scoreboard;
import net.galacticprojects.spigot.util.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class JoinLeaveListener implements Listener {

    CityBuild cityBuild;
    public JoinLeaveListener(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        SQLDatabase database = cityBuild.getSqlConfiguration().getDatabaseRef().get();
        CityBuildPlayer cityBuildPlayer = database.getPlayer(event.getPlayer().getUniqueId()).join();

        if(cityBuildPlayer == null) {
            Random random = new Random();
            cityBuildPlayer = database.createPlayer(event.getPlayer().getUniqueId(), random.nextInt(999999999)).join();
        }

        LinkedList<GalacticBoost> linkedList = new LinkedList<>(cityBuildPlayer.getBoostId(), new GalacticBoost(event.getPlayer(), cityBuildPlayer, GalacticBoostType.GLOBAL_REDUCE_I, database), database);
        if(linkedList.getDynamicArray().length() == 1) {
            GalacticBoost boost = new GalacticBoost(event.getPlayer(), cityBuildPlayer, GalacticBoostType.GLOBAL_FLY_I, database);
        }
        System.out.println(linkedList.getDynamicArray().length());


        Sync sync = database.getSync(event.getPlayer()).join();



        Player player = event.getPlayer();

        IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

        if(sync == null) {
            if(playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName().contains("Farm")) {
                sync = database.createSync(event.getPlayer(), null).join();
                return;
            }
            sync = database.createSync(event.getPlayer(), playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName()).join();
        }

        if(playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getGroups()[0].equals("Farm")) {
            player.getInventory().clear();
        }

        if(sync.isSync()) {
            player.getInventory().clear();
            player.getInventory().setContents(InventoryUtils.restoreModdedStacks(sync.getInventory()));
            player.getInventory().setArmorContents(InventoryUtils.restoreModdedStacks(sync.getArmor()));
            player.setExp((float) sync.getXp());
            player.setLevel(sync.getLevel());
            sync.setSync(false);
        }

        new Scoreboard(event.getPlayer(), cityBuild);

    }
}
