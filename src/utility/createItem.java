package utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import main.MinecraftP;


public class createItem extends ItemStack{

	static MinecraftP plugin;
	public createItem(MinecraftP instance) {
		plugin = instance;
    }
	static ItemStack eye = createItem.getTeleporter();
	static ItemStack dye = createItem.getRegister();
	static ItemStack creator = createItem.getCeator();
	static ItemStack start = createItem.getStart();
	static ItemStack goal = createItem.getGoal();
	static ItemStack quit = createItem.getQuit();
    static ItemStack Up = createItem.getUp();
    static ItemStack guide = createItem.getGuide();
    static ItemStack checkpoint = createItem.getSign();
    static ItemStack config = createItem.getConfigItem();
    static ItemStack close = createItem.getClose();
	 public static ItemStack getSign() {
		 ItemStack signItem = new ItemStack(XMaterial.SIGN.parseItem());
		 BlockStateMeta meta =(BlockStateMeta) signItem.getItemMeta();
		 Sign sign = (Sign) meta.getBlockState();
		 sign.setLine(0, ChatColor.GREEN+"["+ChatColor.GOLD+"CheckPoint"+ChatColor.GREEN+"]");
		 meta.setBlockState(sign);
		 meta.setDisplayName(ChatColor.GOLD+"CheckPoint");
		 List<String> list = new ArrayList<String>();
			String lores0 = ChatColor.BLUE+"It is a sign for";
			String lores1 = ChatColor.BLUE+"setting checkpoint.";
			list.add(0,lores0);
			list.add(1,lores1);
			meta.setLore(list);
		 signItem.setItemMeta(meta);
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
     public static ItemStack getStart()  {
 	    ItemStack start = new ItemStack(XMaterial.LIME_WOOL.parseItem());
 	    ItemMeta meta = start.getItemMeta();
 	     meta.setDisplayName(ChatColor.GOLD+"Set start");
         List<String> lorelist = new ArrayList<String>();
			String lore0 = ChatColor.BLUE+"Right clicking, set start";
			String lore1 = ChatColor.BLUE+"spot of parkours.";
			lorelist.add(0,lore0); lorelist.add(1,lore1);
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
	public static void quidMethod(Player player) {

		if(!player.hasPermission("mp.item.use")) {
    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
    		return;
    		}
		String uuid = player.getUniqueId().toString();
		if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {
			player.sendMessage(ChatColor.GRAY+"You are not playing parkour now.");
			Sound sound= Sound.ENTITY_ITEM_BREAK;
            player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
			return;
		}
		InventoryMethod.InventoryLoad(player);
        plugin.getParkourConfig().set("isPlaying."+uuid, false);
    	plugin.saveParkourConfig();
    	player.setMetadata("TP", new FixedMetadataValue(plugin, 0));
        player.teleport(player.getWorld().getSpawnLocation());
        Sound sound= XSounds.BLOCK_NOTE_BLOCK_XYLOPHONE.parseSounds();
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
	public static void guideMethod(Player player) {
		if(!player.hasPermission("mp.item.use")) {
    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
    		return;
    		}
		String uuid = player.getUniqueId().toString();
		if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {
			player.sendMessage(ChatColor.GRAY+"You are not playing parkour now.");
			Sound sound= Sound.ENTITY_ITEM_BREAK;
            player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
			return;
		}
    	Long goalTime = System.currentTimeMillis();
    	Long startTime = plugin.getParkourConfig().getLong(uuid+".millis");
    	Long Time = goalTime - startTime;
	        Long day = TimeUnit.MILLISECONDS.toDays(Time);
	        Time -= TimeUnit.DAYS.toMillis(day);
	        Long hours = TimeUnit.MILLISECONDS.toHours(Time);
	        Time -= TimeUnit.HOURS.toMillis(hours);
	        Long minutes = TimeUnit.MILLISECONDS.toMinutes(Time);
	        Time -= TimeUnit.MINUTES.toMillis(minutes);
	        Long seconds = TimeUnit.MILLISECONDS.toSeconds(Time);
	        Time -= TimeUnit.SECONDS.toMillis(seconds);
	        if(day!=0) {
	        player.sendMessage(ChatColor.GRAY+""+day+"d, "+hours+"h, "+minutes+"m, "+seconds+"."+Time+"s");
	        }
	       if(day==0&&hours!=0) {
	        player.sendMessage(ChatColor.GRAY+""+hours+"h, "+minutes+"m, "+seconds+"."+Time+"s");
	        }
	      if(day==0&&hours==0&&minutes!=0) {
	        player.sendMessage(ChatColor.GRAY+""+minutes+"m, "+seconds+"."+Time+"s");
	        }
	     if(day==0&&hours==0&&minutes==0) {
	        player.sendMessage(ChatColor.GRAY+""+seconds+"."+Time+"s");
	        }
	    Sound sound= XSounds.BLOCK_NOTE_BLOCK_GUITAR.parseSounds();
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
						    Sound sound= XSounds.BLOCK_NOTE_BLOCK_XYLOPHONE.parseSounds();
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
    Sound sound= Sound.BLOCK_CHEST_CLOSE;
    player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
    }
	public static void rightClick(Player player,PlayerInteractEvent e) {
		if( player.getInventory().getItemInMainHand().equals(eye)
				|| player.getInventory().getItemInOffHand().equals(eye) ) {
			        e.setCancelled(true);
					createItem.teleportMethod(player);

				}
				if( player.getInventory().getItemInMainHand().equals(dye)
						|| player.getInventory().getItemInOffHand().equals(dye) ) {
					if(!player.hasPermission("mp.item.use")) {
			    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
			    		return;
			    		}
					LocationMethod.saveLocation(player);
	                player.sendMessage("Checkpoint was registered.");
	                Sound sound= XSounds.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON.parseSounds();
	                player.playSound(player.getLocation(), sound, 3.0f, 0.5f);
	                e.setCancelled(true);
				}
				if( player.getInventory().getItemInMainHand().equals(creator)
						|| player.getInventory().getItemInOffHand().equals(creator) ) {
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
				if( player.getInventory().getItemInMainHand().equals(quit)
						|| player.getInventory().getItemInOffHand().equals(quit) ) {
					createItem.quidMethod(player);
					 e.setCancelled(true);
	                return;

				}
				if( player.getInventory().getItemInMainHand().equals(Up)
						|| player.getInventory().getItemInOffHand().equals(Up) ) {
					createItem.hideItemMethod(player);
	     	        e.setCancelled(true);
	     	     return;
				}
				if( player.getInventory().getItemInMainHand().equals(guide)
						|| player.getInventory().getItemInOffHand().equals(guide) ) {
					createItem.guideMethod(player);
	     	        e.setCancelled(true);
	     	     return;
				}
				if( player.getInventory().getItemInMainHand().equals(config)
						|| player.getInventory().getItemInOffHand().equals(config) ) {
					Inventory customInventory = Bukkit.createInventory(null, 9,"Config");
					customInventory.setItem(8, close);
					player.openInventory(customInventory);
					 e.setCancelled(true);
	                return;

				}
	}


}
