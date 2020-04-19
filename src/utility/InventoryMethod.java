package utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.MinecraftP;

public class InventoryMethod {
    static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
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

	public static String getNameX(InventoryEvent InventoryE) {
		String name = null;
		if(XMaterial.isMoreThan1_14()) {
				try {
					InventoryView view = (InventoryView)InventoryEvent.class.getMethod("getView").invoke(InventoryE);
					name = view.getTitle();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				return name;
		}
			return InventoryE.getInventory().getName();

	}
	public static void SetStartItem(Player player) {
		 player.getInventory().setItem(0,createItem.getTeleporter());
         player.getInventory().setItem(2,createItem.getQuit());
         player.getInventory().setItem(4,createItem.getGuide());
         player.getInventory().setItem(8,createItem.getConfigItem());
         player.getInventory().setItem(6,createItem.getUp());
	}
   public static void   addOneItem(Player player ,ItemStack item, ItemMeta meta){
       try {
       Class<?> CraftInventory = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftInventory");
       Method firstEmpty = CraftInventory.getMethod("firstEmpty");
       int index = (int)firstEmpty.invoke(player.getInventory());
       if(index == -1) {return;}
       setItem(player,index,item,meta);
       }catch(Exception e) {e.printStackTrace();}

   }
   public static void setItem(Player player ,int index, ItemStack item,ItemMeta meta) {
	   try {
	   Class<?> CraftInventory = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftInventory");
       Method getIInventory=CraftInventory.getMethod("getInventory");
       Object IInventoryOBJ =getIInventory.invoke(player.getInventory());
       Class<?> IInventory = Class.forName("net.minecraft.server."+VERSION+".IInventory");
       Class<?> NMSitemstack = Class.forName("net.minecraft.server."+VERSION+".ItemStack");
       Method setItem = IInventory.getMethod("setItem", int.class,NMSitemstack);
       setItem.setAccessible(true);
       Object stack= asNMScopy(item,meta);
       setItem.invoke(IInventoryOBJ, index,stack);
	   }catch(Exception e) {e.printStackTrace();}
   }
   public static Object asNMScopy(ItemStack stack ,ItemMeta itemmeta) {
	   try {
	   Class<?> CraftMagicNumbers = Class.forName("org.bukkit.craftbukkit."+VERSION+".util.CraftMagicNumbers");
	   Object item;
	   if(XMaterial.isLessThan1_12()) {
	   item = CraftMagicNumbers.getMethod("getItem", org.bukkit.Material.class).invoke(null, stack.getType());
	   }else {
		   item = CraftMagicNumbers.getMethod("getItem", org.bukkit.Material.class,short.class).invoke(null, stack.getType(),stack.getDurability());
	   }
	   Class<?> NMSitemstack = Class.forName("net.minecraft.server."+VERSION+".ItemStack");
	   Class<?> Item = Class.forName("net.minecraft.server."+VERSION+".Item");
	   Object nmsStack;
	   if(XMaterial.isLessThan1_12()) {
	   nmsStack =NMSitemstack.getConstructor(Item,int.class,int.class).newInstance(item,stack.getAmount(),stack.getDurability());
	   }else {
		   Class<?> IMaterial = Class.forName("net.minecraft.server."+VERSION+".IMaterial");
		   nmsStack =NMSitemstack.getConstructor(IMaterial,int.class).newInstance(item,stack.getAmount());
	   }
	   Class<?> NBTTagCompound = Class.forName("net.minecraft.server."+VERSION+".NBTTagCompound");
	   Object nbt =NBTTagCompound.newInstance();
	   Method setTag = NMSitemstack.getMethod("setTag", NBTTagCompound);
	   setTag.invoke(nmsStack, nbt);
	   Class<?> CraftMetaItem = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftMetaItem");
	   Method applyToItem = CraftMetaItem.getDeclaredMethod("applyToItem", NBTTagCompound);
	   applyToItem.setAccessible(true);
	   applyToItem.invoke(itemmeta, nbt);
	   return nmsStack;
	   }catch(Exception e) {e.printStackTrace();}
	   return null;
   }


}
