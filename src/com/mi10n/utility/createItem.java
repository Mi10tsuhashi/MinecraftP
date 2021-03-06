package com.mi10n.utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.mi10n.main.MinecraftP;



public class createItem extends ItemStack{

	static MinecraftP plugin;
	public createItem(MinecraftP instance) {
		plugin = instance;
	}
	static ItemStack eye = createItem.getTeleporter();
	static ItemStack dye = createItem.getRegister();
	static ItemStack creator = createItem.getCeator();
	static ItemStack start = createItem.getStart(null);
	static ItemStack goal = createItem.getGoal();
	static ItemStack quit = createItem.getQuit();
    static ItemStack Up = createItem.getUp();
    static ItemStack guide = createItem.getGuide();
    static ItemStack checkpoint = createItem.getSign();
    static ItemStack config = createItem.getConfigItem();
    static ItemStack close = createItem.getClose();
    @SuppressWarnings("unchecked")
	public static boolean isEnable(Player player,ItemStack item) {
    	if(!player.hasMetadata("disableItem")) { return true;}
    	List<MetadataValue> meta =player.getMetadata("disableItem");
    	for(MetadataValue m : meta) {
    		if(m.getOwningPlugin().equals(plugin)) {
    		ArrayList<ItemStack> disableitems = (ArrayList<ItemStack>) ((FixedMetadataValue)m).value();
    			if(disableitems.contains(item)) {return false;}
    		}

    	}
    	return true;
    }
     public static ItemMeta getSignMeta() {
    	 ItemStack signItem = getSign();
    	 ItemMeta meta = signItem.getItemMeta();
    	 String[] line = { ChatColor.GREEN+"["+ChatColor.GOLD+"CheckPoint"+ChatColor.GREEN+"]","","",""};
			 try {
                final String VERSION =Bukkit.getServer().getClass().getPackage().getName().substring(23);
				Class<?> craftSign = Class.forName("org.bukkit.craftbukkit."+VERSION+".block"+".CraftSign");
				Method sanitizeLines = craftSign.getMethod("sanitizeLines", String[].class);
				Object newLines = sanitizeLines.invoke(null,new Object[] { line});
				Class<?> tileE = Class.forName("net.minecraft.server."+VERSION+".TileEntitySign");
				Object tileSign =tileE.newInstance();
				Object lines = tileE.getField("lines").get(tileSign);
				System.arraycopy(newLines, 0, lines, 0, 4);
				Method save = null;
				Class<?> ItemMeta = null;
				Class<?> NBTTagCompound = Class.forName("net.minecraft.server."+VERSION+".NBTTagCompound");
				if(VERSION.startsWith("v1_8")) {
					save = tileE.getMethod("b", NBTTagCompound);
					if(VERSION.startsWith("v1_8_R1")) {
					ItemMeta = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftMetaTileEntity");
					}else {
						ItemMeta = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftMetaBlockState");
					}
				}else {
					save = tileE.getMethod("save", NBTTagCompound);
					ItemMeta = Class.forName("org.bukkit.craftbukkit."+VERSION+".inventory.CraftMetaBlockState");
				}
                Field blockEntityTag =  ItemMeta.getDeclaredField("blockEntityTag");
                blockEntityTag.setAccessible(true);
                blockEntityTag.set(meta, NBTTagCompound.newInstance());
                Object nbt = blockEntityTag.get(meta);
                save.invoke(tileSign, nbt);
			}catch(InvocationTargetException e2){
				e2.getCause().printStackTrace();
			}
			 catch (Exception e3) {
				e3.printStackTrace();
			}
		 meta.setDisplayName(ChatColor.GOLD+"CheckPoint");
		 List<String> list = new ArrayList<String>();
			String lores0 = ChatColor.BLUE+"It is a sign for";
			String lores1 = ChatColor.BLUE+"setting checkpoint.";
			list.add(0,lores0);
			list.add(1,lores1);
			meta.setLore(list);
			return meta;
     }
	 public static ItemStack getSign() {
		 ItemStack signItem = new ItemStack(XMaterial.SIGN.parseItem());

		 return signItem;
	 }
	 public static ItemStack getClose() {
		 ItemStack close = new ItemStack(Material.BARRIER);
		 ItemMeta meta = close.getItemMeta();
		 meta.setDisplayName("close");
		 close.setItemMeta(meta);
		 return close;
	 }
     public static ItemStack getCeator() {
    	    ItemStack creator = new ItemStack(XMaterial.CLOCK.parseItem());
    	    ItemMeta meta = creator.getItemMeta();
    	    meta.setDisplayName(ChatColor.GOLD+"Creator");
            List<String> lorelist = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"RightClick to open a GUI.";
			lorelist.add(0,lore0);
			meta.setLore(lorelist);
			creator.setItemMeta(meta);
    	 return creator;
     }
     public static ItemStack getToggleOff() {
           ItemStack off = new ItemStack(XMaterial.LIME_DYE.parseItem());
           ItemMeta meta = creator.getItemMeta();
   	    meta.setDisplayName(ChatColor.GOLD+"Toggle disable");
           List<String> lorelist = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"Enable now";
			lorelist.add(0,lore0);
			meta.setLore(lorelist);
			off.setItemMeta(meta);
   	 return off;
     }
     public static ItemStack getToggleOn() {
         ItemStack on = new ItemStack(XMaterial.GRAY_DYE.parseItem());
         ItemMeta meta = creator.getItemMeta();
 	    meta.setDisplayName(ChatColor.GOLD+"Toggle enable");
         List<String> lorelist = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"disable now";
			lorelist.add(0,lore0);
			meta.setLore(lorelist);
			on.setItemMeta(meta);
 	 return on;
   }
     public static ItemStack getTeleporter() {
    	    ItemStack eye = new ItemStack(XMaterial.ENDER_EYE.parseItem());
    	    ItemMeta Meta = eye.getItemMeta();
            Meta.setDisplayName(ChatColor.GOLD+"Teleporter");
			List<String> list = new ArrayList<String>();
			String lores0 = ChatColor.BLUE+"Teleport to a saved";
			String lores1 = ChatColor.BLUE+"checkpoint.";
			list.add(0,lores0);
			list.add(1,lores1);
			Meta.setLore(list);
		    eye.setItemMeta(Meta);
		    return eye;
     }
     public static ItemStack getRegister() {
    	    ItemStack dye = new ItemStack(XMaterial.GRAY_DYE.parseItem());
    	    ItemMeta Meta = dye.getItemMeta();
			Meta.setDisplayName(ChatColor.GOLD+"Register checkpoint");
			List<String> list = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"Save the current position";
			String lore1 = ChatColor.BLUE+"as a checkpoint.";
			list.add(0, lore0);
			list.add(1, lore1);
			Meta.setLore(list);
			dye.setItemMeta(Meta);
    	    return dye;
     }
     public static ItemStack getStart(String name)  {
 	    ItemStack start = new ItemStack(XMaterial.LIME_WOOL.parseItem());
 	    ItemMeta meta = start.getItemMeta();
 	     meta.setDisplayName(ChatColor.GOLD+"Set start");
         List<String> lorelist = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"Right clicking, set start";
			String lore1 = ChatColor.BLUE+"spot of parkours.";
			lorelist.add(0,lore0); lorelist.add(1,lore1);
			if(name != null) {
				String lore2 = ChatColor.BLUE+"ParkourName:"+name;
				lorelist.add(2, lore2);
				}
			meta.setLore(lorelist);
			start.setItemMeta(meta);
 	 return start;
  }
     public static ItemStack getGoal() {
  	     ItemStack Goal = new ItemStack(XMaterial.GOLD_BLOCK.parseItem());
  	     ItemMeta meta = Goal.getItemMeta();
  	     meta.setDisplayName(ChatColor.GOLD+"Set goal");
         List<String> lorelist = new ArrayList<String>();
 	     String lore0 = ChatColor.BLUE+"Right clicking, set goal";
 	     String lore1 = ChatColor.BLUE+"spot of parkours.";
 	     lorelist.add(0,lore0); lorelist.add(1,lore1);
 	     meta.setLore(lorelist);
         Goal.setItemMeta(meta);
  	     return Goal;
   }
     public static ItemStack getGuide() {
    	 ItemStack Guide = new ItemStack(XMaterial.BOOK.parseItem());
    	 ItemMeta meta = Guide.getItemMeta();
    	 meta.setDisplayName(ChatColor.GOLD+"Guide");
		 List<String> lorelist = new ArrayList<String>();
 	     String lore0 = ChatColor.BLUE+"Right click to display";
 	     String lore1 = ChatColor.BLUE+"elapsed time.";
 	     lorelist.add(0,lore0); lorelist.add(1,lore1);
 	     meta.setLore(lorelist);
    	 Guide.setItemMeta(meta);
    	 return Guide;
     }
     public static ItemStack getQuit() {
    	 ItemStack Quit = new ItemStack(XMaterial.ROSE_RED.parseItem());
    	 ItemMeta meta = Quit.getItemMeta();
    	 meta.setDisplayName(ChatColor.GOLD+"Quit");
    	 List<String> lorelist = new ArrayList<String>();
 	     String lore0 = ChatColor.BLUE+"Leave the parkour.";
 	     lorelist.add(0,lore0);
 	     meta.setLore(lorelist);
    	 Quit.setItemMeta(meta);
    	 return Quit;
     }
     public static ItemStack getConfigItem() {
    	 ItemStack Config = new ItemStack(XMaterial.BONE.parseItem());
    	 ItemMeta meta = Config.getItemMeta();
    	 meta.setDisplayName(ChatColor.GOLD+"Config");
    	 Config.setItemMeta(meta);
    	 return Config;
     }
	public static ItemStack getUp() {
    	 ItemStack Up = new ItemStack(XMaterial.GHAST_TEAR.parseItem());
    	 ItemMeta meta = Up.getItemMeta();
    	 meta.setDisplayName(ChatColor.GOLD+"HideItem");
    	 List<String> lorelist = new ArrayList<String>();
 	     String lore0 = ChatColor.BLUE+"Hide items in inventory.";
 	     lorelist.add(0,lore0);
 	     meta.setLore(lorelist);
    	 Up.setItemMeta(meta);
    	 return Up;
     }
	public static ItemStack getShow() {
   	 ItemStack show = new ItemStack(XMaterial.GHAST_TEAR.parseItem());
   	 ItemMeta meta = show.getItemMeta();
   	 meta.setDisplayName(ChatColor.GOLD+"ShowItem");
   	 List<String> lorelist = new ArrayList<String>();
	     String lore0 = ChatColor.BLUE+"Put items back in";
	     String lore1 = ChatColor.BLUE+"the hotbar.";
	     lorelist.add(0,lore0); lorelist.add(1,lore1);
	     meta.setLore(lorelist);
   	 show.setItemMeta(meta);
   	 return show;
    }
	public static void quitMethod(Player player) {

		if(!player.hasPermission("mp.item.use")) {
    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
    		return;
    		}
		String uuid = player.getUniqueId().toString();
		if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {
			player.sendMessage(ChatColor.GRAY+"You are not playing parkour now.");
			Sound sound= XSounds.ENTITY_ITEM_BREAK.parseSound();
            player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
			return;
		}
		 String name =plugin.getParkourConfig().getString(uuid+".parkour");
         if(name!=null) {
         	plugin.getParkourConfig().set(uuid+".parkour",null);
         } else {name = "";}
	    	plugin.saveParkourConfig();
		InventoryMethod.InventoryLoad(player);
        plugin.getParkourConfig().set("isPlaying."+uuid, false);
    	plugin.saveParkourConfig();
    	player.setMetadata("TP", new FixedMetadataValue(plugin, 0));
        player.teleport(player.getWorld().getSpawnLocation());
        Sound sound= XSounds.BLOCK_NOTE_BLOCK_XYLOPHONE.parseSound();
        player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	    player.removePotionEffect(PotionEffectType.SATURATION);
	    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	player.removeMetadata("TP", plugin);
	            }
	        }, 10L);
        return;
	}
	public static String getParkourName(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		if(lore.size()<3) {return null;}
		String parkourname = lore.get(2);
		if(parkourname!=null&&parkourname.split(":").length>1) {
			return parkourname.split(":")[1].trim().substring(2);
		}
		return null;
	}
	public static void guideMethod(Player player) {
		if(!player.hasPermission("mp.item.use")) {
    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
    		return;
    		}
		String uuid = player.getUniqueId().toString();
		if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {
			player.sendMessage(ChatColor.GRAY+"You are not playing parkour now.");
			Sound sound= XSounds.ENTITY_ITEM_BREAK.parseSound();
            player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
			return;
		}
    	Long goalTime = System.currentTimeMillis();
    	Long startTime = plugin.getParkourConfig().getLong(uuid+".millis");
    	Long Time = goalTime - startTime;

	        player.sendMessage(ChatColor.GRAY+com.mi10n.utility.Time.format(Time));

	    Sound sound= XSounds.BLOCK_NOTE_BLOCK_GUITAR.parseSound();
        player.playSound(player.getLocation(), sound, 3.0f, 2.0f);
	}
	public static void teleportMethod(Player player) {
		if(!player.hasPermission("mp.item.use")) {
    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
    		return;
    		}
		String uuid = player.getUniqueId().toString();
		    if(!plugin.getConfig().contains(uuid+".x")) {
			player.sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"[ERROR]"+
		    ChatColor.RED+"There is not your data.");

			}
		    else if(plugin.getConfig().contains(uuid+".x")) {
		         World wt = plugin.getServer().getWorld(plugin.getConfig().getString(uuid+"."
						+"world"));
					        double xt = plugin.getConfig().getDouble(uuid+"."+"x");
						    double yt = plugin.getConfig().getDouble(uuid+"."+"y");
						    double zt = plugin.getConfig().getDouble(uuid+"."+"z");
						    float yawt= (float) plugin.getConfig().getDouble(uuid+"."+"Yaw");
						    float pitcht= (float) plugin.getConfig().getDouble(uuid+"."+"Pitch");
						    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					            @Override
					            public void run() {
					            	player.teleport(new Location(wt,xt,yt,zt,yawt,pitcht));
					            }
					        }, 1L);
						    player.sendMessage(ChatColor.GRAY+"Teleported.");
						    Sound sound= XSounds.BLOCK_NOTE_BLOCK_XYLOPHONE.parseSound();
					        player.playSound(player.getLocation(), sound, 5.0f, 1.0f);

		    }
	}
	public static void hideItemMethod(Player player) {if(!player.hasPermission("mp.item.use")) {
		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
		return;
		}
	Inventory i = player.getInventory();
    i.remove(getUp());
	i.remove(getTeleporter());
	i.remove(getQuit());
	i.remove(getGuide());
	i.remove(getConfigItem());
	i.setItem(27,getTeleporter());
    i.setItem(29,getQuit());
    i.setItem(31,getGuide());
    i.setItem(35,getConfigItem());
    i.setItem(33,getShow());
    Sound sound= XSounds.BLOCK_CHEST_CLOSE.parseSound();
    player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
    }
	private static boolean hasItem(Player player,ItemStack item) {
		try {
		if( player.getInventory().getItemInMainHand().equals(item)
				|| player.getInventory().getItemInOffHand().equals(item) ) {
              return true;
				}
		} catch(NoSuchMethodError error) {
			if(player.getInventory().getItemInHand().equals(item)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	public static void rightClick(Player player,PlayerInteractEvent e) {
		if( hasItem(player,eye) ) {
			        e.setCancelled(true);
			        if(createItem.isEnable(player, eye)){
					createItem.teleportMethod(player);
			        }

				}
				if( hasItem(player,dye)) {
					if(!player.hasPermission("mp.item.use")) {
			    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
			    		return;
			    		}
					LocationMethod.saveLocation(player);
	                player.sendMessage("Checkpoint was registered.");
	                Sound sound= XSounds.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON.parseSound();
	                player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
	                e.setCancelled(true);
				}
				if( hasItem(player,creator) ) {
					if(!player.hasPermission("mp.item.use")) {
			    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
			    		return;
			    		}
					 Inventory customInventory = Bukkit.createInventory(null, 9,"Click to Get!");
					 customInventory.setItem(0, start);
					 customInventory.setItem(1, goal);
					 customInventory.setItem(2, checkpoint);
					 customInventory.setItem(8, close);
					 player.openInventory(customInventory);
				}
				if( hasItem(player,quit) ) {
					if(createItem.isEnable(player, quit)) {
					createItem.quitMethod(player);
					}
					 e.setCancelled(true);
	                return;

				}
				if( hasItem(player,Up))  {
					if(createItem.isEnable(player, Up)) {
					createItem.hideItemMethod(player);
					player.updateInventory();
					}
	     	        e.setCancelled(true);
	     	     return;
				}
				if( hasItem(player,guide) ) {
					if(createItem.isEnable(player, guide)) {
					createItem.guideMethod(player);
					}
	     	        e.setCancelled(true);
	     	     return;
				}
				if( hasItem(player,config) ) {
					Inventory customInventory = Bukkit.createInventory(null, 36,"Config");
					customInventory.setItem(9, eye);
					if(createItem.isEnable(player, eye)) {
						customInventory.setItem(18, createItem.getToggleOff());
						} else {
							customInventory.setItem(18, createItem.getToggleOn());
						}


					customInventory.setItem(11, quit);
					if(createItem.isEnable(player, quit)) {
					customInventory.setItem(20, createItem.getToggleOff());
					} else {
						customInventory.setItem(20, createItem.getToggleOn());
					}

					customInventory.setItem(13, guide);
					if(createItem.isEnable(player, guide)) {
					customInventory.setItem(22, createItem.getToggleOff());
					} else {
						customInventory.setItem(22, createItem.getToggleOn());
					}

					customInventory.setItem(15, createItem.getShow());
					if(createItem.isEnable(player, createItem.getShow())) {
					customInventory.setItem(24, createItem.getToggleOff());
					} else {
						customInventory.setItem(24, createItem.getToggleOn());
					}

					customInventory.setItem(17, Up);
					if(createItem.isEnable(player, Up)) {
					customInventory.setItem(26, createItem.getToggleOff());
					} else {
						customInventory.setItem(26, createItem.getToggleOn());
					}
					customInventory.setItem(8, close);
					player.openInventory(customInventory);
					 e.setCancelled(true);
	                return;

				}

	}


}
