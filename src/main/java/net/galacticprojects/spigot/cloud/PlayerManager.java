package net.galacticprojects.spigot.cloud;

import de.dytanic.cloudnet.ext.bridge.player.NetworkServiceInfo;
import net.galacticprojects.spigot.util.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {

    Player player;

    public PlayerManager(Player player) {
        this.player = player;
    }

    public void sendInventory(NetworkServiceInfo info) {

    }

    public String encodeItems(ItemStack[] items) {
        return InventoryUtils.saveModdedStacksData(items);
    }

    public ItemStack[] decodeItems(String data) throws Exception {
        ItemStack[] it = InventoryUtils.restoreModdedStacks(data);
        if (it == null) {
            it = InventoryUtils.itemStackArrayFromBase64(data);
        }
        return it;
    }

}
