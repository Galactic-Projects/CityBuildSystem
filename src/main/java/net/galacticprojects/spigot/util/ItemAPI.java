package net.galacticprojects.spigot.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemAPI {

    ItemStack itemStack;
    ItemMeta itemMeta;

    public ItemAPI(String name, Material material, byte subid, int amount, String displayname) {
        itemStack = new ItemStack(material, amount, subid);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(BukkitColor.apply(displayname));
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
