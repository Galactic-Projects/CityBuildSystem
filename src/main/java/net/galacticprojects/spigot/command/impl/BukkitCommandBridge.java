package net.galacticprojects.spigot.command.impl;

import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.command.Node;
import me.lauriichan.laylib.command.NodeCommand;
import me.lauriichan.laylib.command.util.Triple;
import me.lauriichan.laylib.localization.MessageManager;
import net.galacticprojects.spigot.CityBuild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public final class BukkitCommandBridge implements CommandExecutor, TabCompleter {

    private final CommandManager commandManager;
    private final MessageManager messageManager;
    private final String prefix;
    private CityBuild system;

    public BukkitCommandBridge(final CommandManager commandManager, CityBuild system, final MessageManager messageManager, final String prefix) {
        this.commandManager = commandManager;
        this.messageManager = messageManager;
        this.prefix = prefix + ':';
        this.system = system;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        while (label.startsWith("/")) {
            label = label.substring(1);
        }
        if (label.startsWith(prefix)) {
            label = label.substring(prefix.length());
        }
        commandManager.createProcess(new BukkitActor<>(sender, system, messageManager), label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        while (label.startsWith("/")) {
            label = label.substring(1);
        }
        if (label.startsWith(prefix)) {
            label = label.substring(prefix.length());
        }
        Triple<NodeCommand, Node, String> triple = commandManager.findNode(label, args);
        if (triple == null) {
            return null;
        }
        if (!triple.getB().hasChildren()) {
            return null;
        }
        return Arrays.asList(triple.getB().getNames());
    }

}
