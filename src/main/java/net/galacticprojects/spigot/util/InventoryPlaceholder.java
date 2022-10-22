package net.galacticprojects.spigot.util;

import com.syntaxphoenix.syntaxapi.utils.java.UniCode;
import me.lauriichan.laylib.localization.MessageManager;
import me.lauriichan.laylib.localization.MessageProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryPlaceholder {

    private InventoryPlaceholder() {
    }

    public static final ItemStack NAVIGATOR;

    static {

        NAVIGATOR = new ItemStack(Material.BEACON);
        ItemMeta meta = NAVIGATOR.getItemMeta();
        meta.setDisplayName(BukkitColor.apply("navigator"));
        NAVIGATOR.setItemMeta(meta);
    }

    public static ItemStack getProfile(Player player) {
        ItemStack profile = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) profile.getItemMeta();
        skullMeta.setDisplayName("profile");
        skullMeta.setOwnerProfile(player.getPlayerProfile());
        profile.setItemMeta(skullMeta);
        return profile;
    }

    public static Inventory getNavigatorInv() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§8§l" + UniCode.ARROWS_RIGHT + " §b§lNAVIGATOR " + "§8§l" + UniCode.ARROWS_LEFT);
        return inventory;
    }

    public static Inventory getGadgetInv() {
        Inventory inventory = Bukkit.createInventory(null, 9, "§8§l" + UniCode.ARROWS_RIGHT + " §5§lGADGETS " + "§8§l" + UniCode.ARROWS_LEFT);
        return inventory;
    }
    public static Inventory getLobbySwitcherInv() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, "§8§l" + UniCode.ARROWS_RIGHT + " §1§lLOBBY SWITCHER " + "§8§l" + UniCode.ARROWS_LEFT);
        return inventory;
    }

    public static ItemStack getTranslatedItem(ItemStack stack, MessageProvider name, String language, MessageManager manager) {
        ItemStack s = new ItemStack(stack.getType());
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(manager.translate(name, language));
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack getTranslatedSkull(ItemStack stack, MessageProvider name, String language, MessageManager manager) {
        ItemStack s = new ItemStack(stack.getType());
        SkullMeta stackMeta = (SkullMeta) stack.getItemMeta();
        SkullMeta meta = (SkullMeta) s.getItemMeta();
        meta.setDisplayName(manager.translate(name, language));
        meta.setOwnerProfile(stackMeta.getOwnerProfile());
        s.setItemMeta(meta);
        return s;
    }
}
