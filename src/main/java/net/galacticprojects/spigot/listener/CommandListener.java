package net.galacticprojects.spigot.listener;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.configuration.caption.Caption;
import com.plotsquared.core.events.PlotMergeEvent;
import com.plotsquared.core.events.Result;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.spigot.CityBuild;
import net.galacticprojects.spigot.config.CommandMessages;
import net.galacticprojects.spigot.database.SQLDatabase;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.database.model.LobbyPlayer;
import net.galacticprojects.spigot.util.array.ArrayType;
import net.galacticprojects.spigot.util.array.SmartArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.Collection;

public class CommandListener implements Listener {

    PlotAPI api;

    CityBuild cityBuild;

    public CommandListener(CityBuild cityBuild) {
        this.cityBuild = cityBuild;
        api = new PlotAPI();
        api.registerListener(this);
    }

    @EventHandler
    public void onCommand(PlayerCommandSendEvent event) {
        String[] arrays = new String[] {"plugins", "help", "?", "pl",
        "plotsquared:p", "plotsquared:ps", "plotsquared:2", "plotsquared:p2", "plotsquared:plotme", "plotsquared:plot", "plotsquared:plots", "plotsquared:plotsquared",
        "bukkit:pl", "bukkit:?", "bukkit:plugins", "Bukkit:pl", "Bukkit:?", "Bukkit:plugins"};
        ArrayList<String> arrayList = new ArrayList<>();

        for(int i = 0; i < arrays.length; i++) {
            arrayList.add(arrays[i]);
        }
        Collection<String> collection = arrayList;
        event.getCommands().removeAll(collection);
    }

    @Subscribe
    public void onMerge(PlotMergeEvent event) {
        SQLDatabase database = cityBuild.getSqlConfiguration().getDatabaseRef().get();
        CityBuildPlayer playerData = database.getPlayer(event.getPlayer().getUUID()).join();

        Player player = Bukkit.getPlayer(event.getPlayer().getUUID());

        LobbyPlayer lobbyPlayer = database.getLobbyPlayer(event.getPlayer().getUUID()).join();

        if(playerData.getMoney() >= 5000000) {
            player.sendMessage(cityBuild.getMessageManager().translate(CommandMessages.CITYBUILD_MERGE_SUCCESSFULLY, lobbyPlayer.getLanguage(), Key.of("remaining", playerData.getMoney() - 5000000)));
            playerData.setMoney(playerData.getMoney() - 5000000);
            event.setEventResult(Result.ACCEPT);
            return;
        }
        player.sendMessage(cityBuild.getMessageManager().translate(CommandMessages.CITYBUILD_MERGE_UNSUCCESSFULLY, lobbyPlayer.getLanguage()));
        event.setEventResult(Result.DENY);
    }

    @EventHandler
    public void onPreproccess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(event.getMessage().equals("/help")) {
            player.sendMessage();
            event.setCancelled(true);
            return;
        }
    }

}
