package utility;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.MinecraftP;

public class InventoryMethod {

	static MinecraftP plugin;
	public InventoryMethod(MinecraftP instance) {
        plugin = instance;
    }

	public static void InventorySave(Player player) {
		 String playername = player.getName();
		 String uuid = player.getUniqueId().toString();
    	 plugin.getInventoryConfig().set(uuid+".PlayerName", playername);
    	 plugin.getInventoryConfig().set(uuid+".Inventory", player.getInventory().getContents());
    	 plugin.getInventoryConfig().set(uuid+".Armor", player.getInventory().getArmorContents());
         plugin.saveInventoryConfig();
	}
	public static void InventoryLoad(Player player) {
		String uuid = player.getUniqueId().toString();
		Object inv =plugin.getInventoryConfig().get(uuid+".Inventory");
        Object arm =plugin.getInventoryConfig().get(uuid+".Armor");
        ItemStack[] inventory = null;
        ItemStack[] armor = null;
        if (inv instanceof ItemStack[]) {
            inventory = (ItemStack[]) inv;
        } else if (inv instanceof List) {
            List<?> Invlist = (List<?>) inv;
            inventory = (ItemStack[]) Invlist.toArray(new ItemStack[0]);
        }
        if (arm instanceof ItemStack[]) {
            armor = (ItemStack[]) arm;
        } else if (arm instanceof List) {
            List<?> Armorlist = (List<?>) arm;
            armor = (ItemStack[]) Armorlist.toArray(new ItemStack[0]);
        }
        player.getInventory().clear();
        player.getInventory().setContents(inventory);
        player.getInventory().setArmorContents(armor);
        player.updateInventory();
	}
	public static void SetStartItem(Player player) {
		 player.getInventory().setItem(0,createItem.getTeleporter());
         player.getInventory().setItem(2,createItem.getQuit());
         player.getInventory().setItem(4,createItem.getGuide());
         player.getInventory().setItem(8,createItem.getConfigItem());
         player.getInventory().setItem(6,createItem.getUp());
	}

}
