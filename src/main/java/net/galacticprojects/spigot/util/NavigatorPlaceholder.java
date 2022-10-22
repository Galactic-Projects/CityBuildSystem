package net.galacticprojects.spigot.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class NavigatorPlaceholder {

    public NavigatorPlaceholder() {}

    public static final ItemStack PLACEHOLDER_ITEM;
    public static final ItemStack RPG_ITEM;
    public static final ItemStack NUCLEAR_WARS_ITEM;
    public static final ItemStack COMMING_SOON_ITEM;
    public static final ItemStack SPAWN_ITEM;

    static {

        PLACEHOLDER_ITEM = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = PLACEHOLDER_ITEM.getItemMeta();
        meta.setDisplayName("§3 ");
        PLACEHOLDER_ITEM.setItemMeta(meta);

        RPG_ITEM = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skmeta = (SkullMeta) RPG_ITEM.getItemMeta();
        skmeta.setOwner("Medievall");
        skmeta.setDisplayName("§c§lNyx");
        RPG_ITEM.setItemMeta(skmeta);

        NUCLEAR_WARS_ITEM = new ItemStack(Material.PLAYER_HEAD);
        skmeta = (SkullMeta) NUCLEAR_WARS_ITEM.getItemMeta();
        skmeta.setOwner("Pankix");
        skmeta.setDisplayName("§a§lProject: Nuclear Wars");
        NUCLEAR_WARS_ITEM.setItemMeta(skmeta);

        COMMING_SOON_ITEM = new ItemStack(Material.BARRIER);
        meta = COMMING_SOON_ITEM.getItemMeta();
        meta.setDisplayName("§4§lComming Soon");
        COMMING_SOON_ITEM.setItemMeta(meta);

        SPAWN_ITEM = new ItemStack(Material.NETHER_STAR);
        meta = SPAWN_ITEM.getItemMeta();
        meta.setDisplayName("§f§lSpawn");
        SPAWN_ITEM.setItemMeta(meta);
    }
}
