package net.galacticprojects.spigot.command;

import me.lauriichan.laylib.command.annotation.*;
import me.lauriichan.laylib.localization.Key;
import net.galacticprojects.spigot.CityBuild;
import net.galacticprojects.spigot.command.impl.BukkitActor;
import net.galacticprojects.spigot.config.CommandMessages;
import net.galacticprojects.spigot.database.model.CityBuildPlayer;
import net.galacticprojects.spigot.database.model.LobbyPlayer;
import net.galacticprojects.spigot.util.ComponentParser;
import net.galacticprojects.spigot.util.MojangProfileService;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

@Command(name = "money", description = "See your coins")
public class MoneyCommand {

    @Action("show")
    public void coins(BukkitActor<?> actor, CityBuild plugin, @Argument(name = "player", optional = true) String name) {
        Player player = actor.as(Player.class).getHandle();
        UUID uniqueId = player.getUniqueId();

        if (name != null) {
            UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

            plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
                sql.getPlayer(uniqueIdTarget).thenAccept(targetData -> {
                    String coins = formatValue(targetData.getMoney());
                    actor.sendTranslatedMessage(CommandMessages.COINS_SHOW_OTHERS, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("input", coins));
                });
            });
            return;
        }

        plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                String coins = formatValue(playerData.getMoney());
                actor.sendTranslatedMessage(CommandMessages.COINS_SHOW, Key.of("input", coins));
            });
        });
    }

    @Action("pay")
    public void pay(BukkitActor<?> actor, CityBuild plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1)
    }) int amount) {
        Player player = actor.as(Player.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                Player target = Bukkit.getPlayer(uniqueIdTarget);
                CityBuildPlayer targetData = sql.getPlayer(uniqueIdTarget).join();
                LobbyPlayer targetSystemData = sql.getLobbyPlayer(uniqueIdTarget).join();

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if (amount > playerData.getMoney()) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_ENOUGH);
                        return;
                    }

                    if(target == player || uniqueIdTarget == uniqueId){
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SELF);
                        return;
                    }

                    int playerCoins = playerData.getMoney();
                    int targetCoins = targetData.getMoney();

                    playerCoins = (playerCoins - amount);
                    targetCoins = (targetCoins + amount);
                    playerData.setMoney(playerCoins);
                    targetData.setMoney(targetCoins);

                    sql.updatePlayer(playerData);
                    sql.updatePlayer(targetData);

                    String amountFormat = formatValue(amount);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_SENT_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("amount", amountFormat));
                    if (target != null) {
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COINS_OTHERS_SENT_TARGET, targetSystemData.getLanguage(), Key.of("player", actor.getName()), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });

    }

    @Action("set")
    @Permission("system.command.coins")
    public void set(BukkitActor<?> actor, CityBuild plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        Player player = actor.as(Player.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                Player target = Bukkit.getPlayer(uniqueIdTarget);
                CityBuildPlayer targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getMoney();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if (amount == targetData.getMoney()) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_ALREADY);
                        return;
                    }

                    String amountFormat = formatValue(amount);

                    targetData.setMoney(amount);
                    sql.updatePlayer(targetData);
                    LobbyPlayer targetSystemData = sql.getLobbyPlayer(uniqueIdTarget).join();
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_SET_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COINS_OTHERS_SET_TARGET, targetSystemData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("add")
    @Permission("system.command.coins")
    public void add(BukkitActor<?> actor, CityBuild plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        Player player = actor.as(Player.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                Player target = Bukkit.getPlayer(uniqueIdTarget);
                CityBuildPlayer targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getMoney();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    int targetCoins = targetData.getMoney();
                    targetCoins = (targetCoins + amount);
                    String amountFormat = formatValue(targetCoins);

                    targetData.setMoney(targetCoins);
                    sql.updatePlayer(targetData);
                    LobbyPlayer targetSystemData = sql.getLobbyPlayer(uniqueIdTarget).join();
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_ADD_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COINS_OTHERS_ADD_TARGET, targetSystemData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    @Action("remove")
    @Permission("system.command.coins")
    public void remove(BukkitActor<?> actor, CityBuild plugin, @Argument(name = "player") String name, @Argument(name = "amount", params = {
            @Param(type = 3, name="minimum", intValue = 1),
    }) int amount) {
        Player player = actor.as(Player.class).getHandle();
        UUID uniqueId = player.getUniqueId();
        UUID uniqueIdTarget = MojangProfileService.getUniqueId(name);

        plugin.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(uniqueId).thenAccept(playerData -> {
                Player target = Bukkit.getPlayer(uniqueIdTarget);
                CityBuildPlayer targetData = sql.getPlayer(uniqueIdTarget).join();
                int oldA = targetData.getMoney();
                String oldAmount = formatValue(oldA);

                try {
                    if (amount <= 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NEGATIVE);
                        return;
                    }

                    if(oldA < amount) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SMALL);
                        return;
                    }

                    int targetCoins = targetData.getMoney();
                    targetCoins = (targetCoins - amount);
                    String amountFormat = formatValue(targetCoins);

                    if(targetCoins < 0) {
                        actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_SMALL);
                        return;
                    }

                    LobbyPlayer targetSystemData = sql.getLobbyPlayer(uniqueIdTarget).join();

                    targetData.setMoney(targetCoins);
                    sql.updatePlayer(targetData);
                    actor.sendTranslatedMessage(CommandMessages.COINS_OTHERS_REMOVE_PLAYER, Key.of("player", MojangProfileService.getName(uniqueIdTarget)), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat));
                    if (target != null || uniqueIdTarget != uniqueId) {
                        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, ComponentParser.parse(plugin.getMessageManager().translate(CommandMessages.COINS_OTHERS_REMOVE_TARGET, targetSystemData.getLanguage(), Key.of("player", player.getName()), Key.of("oldamount", oldAmount), Key.of("amount", amountFormat))));
                    }
                } catch (NumberFormatException e) {
                    actor.sendTranslatedMessage(CommandMessages.COINS_ERRORS_NUMBER);
                }
            });
        });
    }

    private String formatValue(int value) {
        String format = Integer.toString(value);
        DecimalFormat decimal = new DecimalFormat();
        format = decimal.format((double) Integer.parseInt(format));
        return format;
    }
}
