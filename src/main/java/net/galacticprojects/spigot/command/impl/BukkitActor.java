package net.galacticprojects.spigot.command.impl;

import me.lauriichan.laylib.command.Action;
import me.lauriichan.laylib.command.ActionMessage;
import me.lauriichan.laylib.command.Actor;
import me.lauriichan.laylib.localization.Key;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.MessageProvider;
import net.galacticprojects.lobbysystem.LobbySystem;
import net.galacticprojects.lobbysystem.command.impl.version.NbtProvider;
import net.galacticprojects.lobbysystem.database.SQLDatabase;
import net.galacticprojects.lobbysystem.database.model.LobbyPlayer;
import net.galacticprojects.lobbysystem.util.ComponentParser;
import net.galacticprojects.lobbysystem.util.color.ComponentColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BukkitActor<P extends CommandSender> extends Actor<P> {

    private final LobbySystem system;
    private String language;

    public BukkitActor(final P handle, final LobbySystem system, final MessageManager messageManager) {
        super(handle, messageManager);
        this.system = system;
    }

    @Override
    public UUID getId() {
        Actor<Entity> actor = as(Entity.class);
        if (actor.isValid()) {
            return actor.getHandle().getUniqueId();
        }
        return IMPL_ID;
    }

    @Override
    public String getName() {
        Actor<Entity> actor = as(Entity.class);
        if (actor.isValid()) {
            Entity entity = actor.getHandle();
            if (entity.getCustomName() == null) {
                return entity.getName();
            }
            return entity.getCustomName();
        }
        return handle.getName();
    }

    @Override
    public String getLanguage() {
        if(language != null) {
            return language;
        }
        SQLDatabase database = system.getSqlConfiguration().getDatabaseRef().get();
        if(database == null) {
            return super.getLanguage();
        }
        LobbyPlayer player = database.getPlayer(getId()).join();
        if(player == null) {
            return language = super.getLanguage();
        }
        return language = player.getLanguage();
    }

    public void setLanguage(String language) {
        this.language = language;
        system.getSqlConfiguration().getDatabaseRef().asOptional().ifPresent(sql -> {
            sql.getPlayer(getId()).thenAccept(player -> {
                if(player == null){
                    return;
                }
                player.setLanguage(language);
                sql.updatePlayer(player);
            });
        });
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(ComponentColor.apply(message));
    }

    @Override
    public void sendTranslatedMessage(String messageId, Key... placeholders) {
        sendMessage(messageManager.translate(messageId, getLanguage(), placeholders));
    }

    @Override
    public void sendTranslatedMessage(MessageProvider provider, Key... placeholders) {
        sendMessage(messageManager.translate(provider, getLanguage(), placeholders));
    }

    @Override
    public void sendActionMessage(ActionMessage message) {
        if (message == null) {
            return;
        }
        String content = message.message();
        if (content == null || content.isBlank()) {
            handle.sendMessage("");
            return;
        }
        ClickEvent click = null;
        HoverEvent hover = null;
        if (message.clickAction() != null) {
            Action clickAction = message.clickAction();
            switch (clickAction.getType()) {
                case CLICK_COPY:
                    click = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickAction.getValueAsString());
                    break;
                case CLICK_FILE:
                    click = new ClickEvent(ClickEvent.Action.OPEN_FILE, clickAction.getValueAsString());
                    break;
                case CLICK_RUN:
                    click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickAction.getValueAsString());
                    break;
                case CLICK_SUGGEST:
                    click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickAction.getValueAsString());
                    break;
                case CLICK_URL:
                    click = new ClickEvent(ClickEvent.Action.OPEN_URL, clickAction.getValueAsString());
                    break;
                default:
                    break;
            }
        }
        if (message.hoverAction() != null) {
            Action hoverAction = message.hoverAction();
            switch (hoverAction.getType()) {
                case HOVER_SHOW:
                    if (hoverAction.getValue() instanceof ItemStack) {
                        ItemStack item = (ItemStack) hoverAction.getValue();
                        hover = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Item(item.getType().getKey().toString(), item.getAmount(),
                                ItemTag.ofNbt(NbtProvider.itemStackToNbt(item).toString())));
                    }
                    break;
                case HOVER_TEXT:
                    hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ComponentParser.parse(hoverAction.getValueAsString())));
                    break;
                default:
                    break;
            }
        }
        handle.spigot().sendMessage(ComponentParser.parse(message.message(), click, hover));
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

}