package net.galacticprojects.spigot;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.lauriichan.laylib.command.ArgumentRegistry;
import me.lauriichan.laylib.command.CommandManager;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.source.AnnotationMessageSource;
import me.lauriichan.laylib.localization.source.EnumMessageSource;
import me.lauriichan.laylib.logger.ISimpleLogger;
import me.lauriichan.laylib.logger.JavaSimpleLogger;
import net.galacticprojects.spigot.command.CityBuildCommand;
import net.galacticprojects.spigot.command.FarmweltCommand;
import net.galacticprojects.spigot.command.MoneyCommand;
import net.galacticprojects.spigot.command.impl.BukkitCommandInjector;
import net.galacticprojects.spigot.command.provider.CityBuildProvider;
import net.galacticprojects.spigot.command.provider.SQLProvider;
import net.galacticprojects.spigot.config.CommandMessages;
import net.galacticprojects.spigot.config.SQLConfiguration;
import net.galacticprojects.spigot.listener.CommandListener;
import net.galacticprojects.spigot.listener.JoinLeaveListener;
import net.galacticprojects.spigot.message.CommandManagerMessages;
import net.galacticprojects.spigot.message.MessageProviderFactoryImpl;
import net.galacticprojects.spigot.message.MessageTranslationManager;
import net.galacticprojects.spigot.util.source.Resources;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class CityBuild extends JavaPlugin {
    private ISimpleLogger logger;
    private MessageManager messageManager;
    private MessageProviderFactoryImpl messageProviderFactory;

    private MessageTranslationManager translationManager;

    private Resources resources;
    private CommandManager commandManager;
    private SQLConfiguration sqlConfiguration;

    private static PlotAPI api;

    int percent = 1;

    @Override
    public void onLoad() {
        this.logger = new JavaSimpleLogger(getLogger());
        this.resources = new Resources(getDataFolder(), getFile(), getLogger());
        this.commandManager = new CommandManager(this.logger);
        this.messageManager = new MessageManager();
        this.messageProviderFactory = new MessageProviderFactoryImpl(logger);
        this.translationManager =  new MessageTranslationManager(messageManager, logger, getDataFolder());
        registerMessages();
        translationManager.reload();
        createConfigurations();
        reloadConfigurations();
    }

    @Override
    public void onEnable() {
        commandManager.setInjector(new BukkitCommandInjector(commandManager, this, messageManager, this));
        registerArgumentTypes();
        registerListener();
        registerCommands();

        api = new PlotAPI();
    }

    @Override
    public void onDisable() {
        sqlConfiguration.getDatabaseRef().asOptional().ifPresent(database -> database.shutdown());
    }

    private void createConfigurations() {
        sqlConfiguration = new SQLConfiguration(logger, getDataFolder());
    }

    public void reloadConfigurations() {
        sqlConfiguration.reload();
    }
    private void registerMessages() {
        messageManager.register(new EnumMessageSource(CommandManagerMessages.class, messageProviderFactory));
        messageManager.register(new AnnotationMessageSource(CommandMessages.class, messageProviderFactory));
    }
    private void registerArgumentTypes() {
        ArgumentRegistry argumentRegistry = commandManager.getRegistry();
        argumentRegistry.setProvider(new CityBuildProvider(this));
        argumentRegistry.setProvider(new SQLProvider(sqlConfiguration));
    }

    private void registerCommands() {
        commandManager.register(FarmweltCommand.class);
        commandManager.register(CityBuildCommand.class);
        commandManager.register(MoneyCommand.class);
    }

    private void registerListener() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new JoinLeaveListener(this), this);
        manager.registerEvents(new CommandListener(this), this);
    }

    public MessageTranslationManager getTranslationManager() {return translationManager;}

    public CommandManager getCommandManager() {
        return commandManager;
    }


    public SQLConfiguration getSqlConfiguration() {
        return sqlConfiguration;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static PlotAPI getAPI() {
        return api;
    }
}
