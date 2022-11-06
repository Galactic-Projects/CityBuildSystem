package net.galacticprojects.spigot.config;

import me.lauriichan.laylib.localization.MessageProvider;
import me.lauriichan.laylib.localization.source.Message;

public class CommandMessages {

    private CommandMessages() {
        throw new UnsupportedOperationException();
    }

    @Message(id = "plugin.name", content = "&4&lSYSTEM")
    public static MessageProvider PLUGIN_NAME;
    @Message(id = "plugin.prefix", content = "&8「 $#plugin.name &8」&r")
    public static MessageProvider PLUGIN_PREFIX;

    @Message(id = "command.build.left", content = "$#plugin.prefix §7You §cleft §7the §bBuildmode")
    public static MessageProvider COMMAND_BUILD_LEFT;
    @Message(id = "command.build.entered", content = "$#plugin.prefix §7You §aentered §7the §bBuildmode")
    public static MessageProvider COMMAND_BUILD_ENTERED;

    @Message(id = "command.help.command.none", content = "$#plugin.prefix The command '&c$command&7' doesn't exist!")
    public static MessageProvider COMMAND_HELP_NONE;
    @Message(id = "command.help.command.empty", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_EMPTY;
    @Message(id = "command.help.command.tree", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command has following subcommands:",
            "$tree",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_TREE;
    @Message(id = "command.help.command.executable", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command can be executed.",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_EXECUATABLE;
    @Message(id = "command.help.command.tree-executable", content = {
            "&8=< $#plugin.name &8>-< &7$command",
            " ",
            "&7$description",
            " ",
            "&7This command can be executed and has following subcommands:",
            "$tree",
            " ",
            "&8=< $#plugin.name &8>-< &7$command"
    })
    public static MessageProvider COMMAND_HELP_TREE$EXECUTABLE;
    @Message(id = "command.help.tree.format", content = "&8- &7$name")
    public static MessageProvider COMMAND_HELP_TREE_FORMAT;

    @Message(id = "command.translation.reload.start", content = "$#plugin.prefix Translation reload started")
    public static MessageProvider COMMAND_TRANSLATION_RELOAD_START;
    @Message(id = "command.translation.reload.end", content = {
            "&8=< $#plugin.name &8>-< &7Translations",
            " ",
            "&8Newly loaded: &7$new",
            "&8Reloaded: &7$loaded",
            "&8Unloaded: &7$unloaded"
    })
    public static MessageProvider COMMAND_TRANSLATION_RELOAD_END;

    @Message(id = "citybuild.name", content = "§e§lCITYBUILD")
    public static MessageProvider CITYBUILD_NAME;
    @Message(id = "citybuild.prefix", content = "§8「 $#citybuild.name §8」§7")
    public static MessageProvider CITYBUILD_PREFIX;

    @Message(id = "citybuild.merge.question", content = "$#citybuild.prefix Are you sure that you want to pay 5 Million to merge your Plots? Write Yes or No.")
    public static MessageProvider CITYBUILD_MERGE_QUESTION;

    @Message(id = "citybuild.merge.successfully", content = "$#citybuild.prefix You paid 5 Million for a Plot Merge")
    public static MessageProvider CITYBUILD_MERGE_SUCCESSFULLY;
    @Message(id = "citybuild.merge.unsuccessfully", content = "$#citybuild.prefix You have not enough Money to Merge your Plots")
    public static MessageProvider CITYBUILD_MERGE_UNSUCCESSFULLY;
    @Message(id = "command.citybuild.null", content = "$#citybuild.prefix we can't find any CityBuild you was on before, please go to the Lobby and connect to one of the CityBuild Servers")
    public static MessageProvider COMMAND_CITYBUILD_NULL;
    @Message(id = "command.citybuild.already", content = "$#citybuild.prefix You're already connected to a CityBuild Server")
    public static MessageProvider COMMAND_CITYBUILD_ALREADY;
    @Message(id = "command.farmwelt.already", content = "$#citybuild.prefix You're already connected to a Farmworld Server")
    public static MessageProvider COMMAND_FARMWORLD_ALREADY;


    @Message(id = "coins.prefix", content = "&8&l「 &a&lMoney &8&l」&7")
    public static MessageProvider COINS_PREFIX;

    @Message(id = "command.coins.errors.negative", content = "$#coins.prefix &cThe amount must be over zero!")
    public static MessageProvider COINS_ERRORS_NEGATIVE;

    @Message(id = "command.coins.errors.enough", content = "$#coins.prefix &cNot enough Money!")
    public static MessageProvider COINS_ERRORS_ENOUGH;

    @Message(id = "command.coins.errors.already", content = "$#coins.prefix &cThis player has already the amount that you want to set!")
    public static MessageProvider COINS_ERRORS_ALREADY;
    @Message(id = "command.coins.errors.number", content = "$#coins.prefix &cFormat must be a number!")
    public static MessageProvider COINS_ERRORS_NUMBER;

    @Message(id = "command.coins.errors.small", content = "$#level.prefix &cYou can't remove this amount from the player money, because it will be smaller than 0!")
    public static MessageProvider COINS_ERRORS_SMALL;

    @Message(id = "command.coins.errors.number", content = "$#coins.prefix &cYou can't send yourself money!")
    public static MessageProvider COINS_ERRORS_SELF;

    @Message(id = "command.coins.name", content = "Money")
    public static MessageProvider COINS_NAME;

    @Message(id = "command.coins.show.single", content = "$#coins.prefix &7Your &amoney &7are &b$input $#command.coins.name&7.")
    public static MessageProvider COINS_SHOW;

    @Message(id = "command.coins.show.others", content = "$#coins.prefix &7The &amoney &7by &c$player &7is &b$input $#command.coins.name&7.")
    public static MessageProvider COINS_SHOW_OTHERS;

    @Message(id = "command.coins.pay.sent.player", content = "$#coins.prefix &7You &3sent &c$player &b$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_SENT_PLAYER;

    @Message(id = "command.coins.pay.sent.target", content = "$#coins.prefix &b$player &csent &7you &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_SENT_TARGET;

    @Message(id = "command.coins.set.player", content = "$#coins.prefix &7You &7set &b$player &7a new amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_SET_PLAYER;

    @Message(id = "command.coins.set.target", content = "$#coins.prefix &b$player &7set you a new amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_SET_TARGET;

    @Message(id = "command.coins.add.player", content = "$#coins.prefix &7You &7added &b$player &7an amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_ADD_PLAYER;

    @Message(id = "command.coins.add.target", content = "$#coins.prefix &b$player &7added you an amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_ADD_TARGET;

    @Message(id = "command.coins.remove.player", content = "$#coins.prefix &7You &7removed &b$player &7an amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_REMOVE_PLAYER;

    @Message(id = "command.coins.remove.target", content = "$#coins.prefix &b$player &7removed you an amount of &amoney &7from &3$oldamount &7to &3$amount $#command.coins.name&7.")
    public static MessageProvider COINS_OTHERS_REMOVE_TARGET;
}
