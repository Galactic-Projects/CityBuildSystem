package net.galacticprojects.spigot.command;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import me.lauriichan.laylib.command.annotation.Action;
import me.lauriichan.laylib.command.annotation.Argument;
import me.lauriichan.laylib.command.annotation.Command;
import net.galacticprojects.spigot.CityBuild;
import net.galacticprojects.spigot.command.impl.BukkitActor;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.model.Sync;
import net.galacticprojects.spigot.util.InventoryUtils;
import org.bukkit.entity.Player;

@Command(name = "cb", aliases = {
        "city",
        "citybuild"
})
public class CityBuildCommand {

    @Action("")
    public void onExecute(BukkitActor<?> actor, CityBuild cityBuild) {
        Player player = actor.as(Player.class).getHandle();
        SQLDatabase database = cityBuild.getSqlConfiguration().getDatabaseRef().get();

        IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

        Sync sync = database.getSync(player).join();

        if(database.getSync(player).join() == null) {
            sync = database.createSync(player, playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName()).join();
            sync.update(InventoryUtils.saveModdedStacksData(player.getInventory().getArmorContents()), InventoryUtils.saveModdedStacksData(player.getInventory().getContents()), player.getExp(), player.getLevel(), playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName(), true);
            playerManager.getOnlinePlayer(player.getUniqueId()).getPlayerExecutor().connectToGroup("Nature", ServerSelectorType.RANDOM);
            return;
        }

        if(sync.getLast_service() == null) {
            actor.sendTranslatedMessage("command.citybuild.null");
            return;
        }

        if(playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName().equals(sync.getLast_service())) {
            actor.sendTranslatedMessage("command.citybuild.already");
            return;
        }
        playerManager.getOnlinePlayer(player.getUniqueId()).getPlayerExecutor().connect(sync.getLast_service());
        sync.update(InventoryUtils.saveModdedStacksData(player.getInventory().getArmorContents()), InventoryUtils.saveModdedStacksData(player.getInventory().getContents()), player.getExp(), player.getLevel(), playerManager.getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName(), true);
    }

}
