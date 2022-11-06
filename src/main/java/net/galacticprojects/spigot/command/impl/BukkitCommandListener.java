package net.galacticprojects.spigot.command.impl;

import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.command.CommandProcess;
import me.lauriichan.laylib.localization.MessageManager;
import net.galacticprojects.spigot.CityBuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class BukkitCommandListener implements Listener {

    private final CommandManager commandManager;
    private final MessageManager messageManager;
    CityBuild system;

    public BukkitCommandListener(final CommandManager commandManager, CityBuild system, final MessageManager messageManager) {
        this.commandManager = commandManager;
        this.messageManager = messageManager;
        this.system = system;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        CommandProcess process = commandManager.getProcess(player.getUniqueId());
        if (process == null) {
            return;
        }
        event.setCancelled(true);
        commandManager.handleProcessInput(new BukkitActor<>(player, system, messageManager), process, event.getMessage(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        CommandProcess process = commandManager.getProcess(player.getUniqueId());
        if (process == null) {
            return;
        }
        event.setCancelled(true);
        BukkitActor<Player> actor = new BukkitActor<>(player, system, messageManager);
        String[] args = event.getMessage().split(" ");
        if (args[0].equalsIgnoreCase("/cancel")) {
            commandManager.cancelProcess(actor);
            return;
        }
        if (args[0].equalsIgnoreCase("/skip")) {
            commandManager.handleProcessSkip(actor, process);
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("/suggestion")) {
            commandManager.handleProcessInput(actor, process,
                    Arrays.stream(args).skip(1).filter(Predicate.not(String::isBlank)).collect(Collectors.joining(" ")), true);
            return;
        }
        commandManager.handleProcessInput(actor, process, event.getMessage());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event) {
        CommandProcess process = commandManager.getProcess(Actor.IMPL_ID);
        if (process == null) {
            return;
        }
        BukkitActor<CommandSender> actor = new BukkitActor<>(event.getSender(), system, messageManager);
        event.setCancelled(true);
        String[] args = event.getCommand().split(" ");
        if (args[0].equalsIgnoreCase("/cancel")) {
            commandManager.cancelProcess(actor);
            return;
        }
        if (args[0].equalsIgnoreCase("/skip")) {
            commandManager.handleProcessSkip(actor, process);
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("/suggestion")) {
            commandManager.handleProcessInput(actor, process,
                    Arrays.stream(args).skip(1).filter(Predicate.not(String::isBlank)).collect(Collectors.joining(" ")), true);
            return;
        }
        commandManager.handleProcessInput(actor, process, event.getCommand());
    }

}
