package com.mi10n.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.Sets;
import com.mi10n.main.MinecraftP;
import com.mi10n.utility.InventoryMethod;
import com.mi10n.utility.LocationMethod;
import com.mi10n.utility.XMaterial;
import com.mi10n.utility.XSounds;
import com.mi10n.utility.createItem;

import net.wesjd.anvilgui.AnvilGUI;








public class Eventlis implements Listener{
	 MinecraftP plugin;

	    public Eventlis(MinecraftP instance) {
	        plugin = instance;
	    }

	@EventHandler
	public void disableChest(PlayerInteractEvent e) {
    	if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    		Block block = e.getClickedBlock();
    		Player player = e.getPlayer();
        	World world = player.getWorld();
    		if(block.getType()==Material.CHEST) {
    			if(plugin.getWorldConfig().getBoolean("World:"+world+".DisableChest")) {
    				String name = player.getName();
    				player.sendMessage(name+ChatColor.GREEN+": "+ChatColor.WHITE+"It seems better not to open the chest ...");
    			    e.setCancelled(true);}
    		}
    	}
	}
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player) {
		Player player = (Player) e.getEntity();
		String uuid = player.getUniqueId().toString();
		if(e.getEntity().hasMetadata("TP")) {e.setCancelled(true);}
		if(plugin.getParkourConfig().getBoolean("isPlaying."+uuid)&&e.getCause() == DamageCause.FALL){
            e.setCancelled(true);
        }
	 }
	}
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {return;}
		ItemStack item = e.getItemDrop().getItemStack();
		if(item.equals(createItem.getTeleporter())||item.equals(createItem.getQuit())||item.equals(createItem.getGuide())
				||item.equals(createItem.getUp())||item.equals(createItem.getConfigItem())||item.equals(createItem.getUp())) {
			e.setCancelled(true);
			return;
			}
	}
	@EventHandler
    public void timeResetEvent(PlayerInteractEvent e) {

		if(e.getAction() != Action.PHYSICAL) {
			return;
			}
    	if((e.getClickedBlock().getType()==XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial())){
    		Player player =e.getPlayer();
    		Double x = e.getClickedBlock().getLocation().getX();
    	    Double y = e.getClickedBlock().getLocation().getY();
    	    Double z = e.getClickedBlock().getLocation().getZ();
    	    String X = String.valueOf(x);
    	    String Y = String.valueOf(y);
    	    String Z = String.valueOf(z);
    	    String uuid = player.getUniqueId().toString();
    	    if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {return;}
    		if(!plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")")){return;}
    		if(!player.hasMetadata("press")) {
    			player.setMetadata("press",new FixedMetadataValue(plugin, 0));
    	        plugin.getParkourConfig().set(uuid+".millis",System.currentTimeMillis());
    	        String pN = player.getName();
 				plugin.getParkourConfig().set(uuid+".Name",pN);
 				plugin.saveParkourConfig();
    		 }
    		return;
    		}
    }
	@EventHandler
	public void resetEvent(PlayerMoveEvent e) {
		if(!e.getPlayer().hasMetadata("press")) {
			return;
		}else {
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	if(!(e.getPlayer().getLocation().getBlock().getType()==XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial())) {
	    				e.getPlayer().removeMetadata("press", plugin);
	    			}
	            }
	        }, 10L);

		}
	}
	@EventHandler
    public void presureEvent(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL) {
    	if((e.getClickedBlock().getType()==XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial())){
    		Player player = e.getPlayer();
    		Double x = e.getClickedBlock().getLocation().getX();
    	    Double y = e.getClickedBlock().getLocation().getY();
    	    Double z = e.getClickedBlock().getLocation().getZ();
    	    String X = String.valueOf(x);
    	    String Y = String.valueOf(y);
    	    String Z = String.valueOf(z);
    	    String uuid = player.getUniqueId().toString();
    	    if(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")")){
    	    	if(plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {return;}
    	    	String corr = "("+X+")("+Y+")("+Z+")";
    	    	corr = corr.replace(".", "-");
                 plugin.getParkourConfig().set("isPlaying."+uuid, true);
                 plugin.saveParkourConfig();
                 InventoryMethod.InventorySave(player);
                 player.getInventory().clear();
                 InventoryMethod.SetStartItem(player);
 				 String pN = player.getName();
 				 LocationMethod.saveLocation(player);
 				String name =plugin.getParkourConfig().getString(corr);
                 if(name!=null) {
                	 plugin.getParkourConfig().set(uuid+".parkour",name);
                 } else {
                	 plugin.getParkourConfig().set(uuid+".parkour",null);
                 }
 				 plugin.getParkourConfig().set(uuid+".millis",System.currentTimeMillis());
 				 plugin.getParkourConfig().set(uuid+".Name",pN);
 				 plugin.saveParkourConfig();
 				 player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147000, 100));
 				 player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2147000, 100));
    	    	return;}
    	    if(plugin.getParkourConfig().getStringList("goal").contains("("+X+")("+Y+")("+Z+")")){
    	    	if(!plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {return;}
    	    	InventoryMethod.InventoryLoad(player);
                plugin.getParkourConfig().set("isPlaying."+uuid, false);
                String name =plugin.getParkourConfig().getString(uuid+".parkour");
                String pn = player.getName();
    	    	long goalTime = System.currentTimeMillis();
    	    	long startTime = plugin.getParkourConfig().getLong(uuid+".millis");
    	    	long Time = goalTime - startTime;
    	    	String formatname;
                if(name!=null) {
                	formatname = " "+name+" ";
                	plugin.getParkourConfig().set(uuid+".parkour",null);
                } else {formatname = " ";}
    	    	plugin.saveParkourConfig();

     	    	 Bukkit.broadcastMessage(ChatColor.AQUA+""+pn+" cleared"+formatname+"Parkour! "
         	        		+ChatColor.GRAY+com.mi10n.utility.Time.format(Time));
                if(name!=null) {
                	 if(plugin.getDatabase().isBest(player, name, Time)&&plugin.getDatabase().getBest(player, name)>0) {
                     	plugin.getDatabase().updateTime(player, plugin.getDatabase().getCourseId(name), Time);
                     	player.sendMessage(ChatColor.GRAY+"this is best!");
                     }else if(plugin.getDatabase().getBest(player, name)<0){
                     	plugin.getDatabase().insertTime(player, plugin.getDatabase().getCourseId(name), Time);
                     	player.sendMessage(ChatColor.GRAY+"this is first record.");
                     } else {
                     	player.sendMessage(ChatColor.GRAY+"your best is: "+com.mi10n.utility.Time.format(plugin.getDatabase().getBest(player, name)));
                     }
                }
     	        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
     	        player.removePotionEffect(PotionEffectType.SATURATION);
    	    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    	        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
    	            @Override
    	            public void run() {
    	            	player.teleport(player.getWorld().getSpawnLocation());
    	            }
    	        }, 5L);


    	    	}
    	 }


    	    	return;


    	}



    }
	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		if(!(e.getBlock().getType().equals(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial()))) {return;}
		Double x = e.getBlock().getLocation().getX();
	    Double y = e.getBlock().getLocation().getY();
	    Double z = e.getBlock().getLocation().getZ();
	    String X = String.valueOf(x);
	    String Y = String.valueOf(y);
	    String Z = String.valueOf(z);
	    Player player = e.getPlayer();
	    if(!(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")"))){
	    	if(!(plugin.getParkourConfig().getStringList("goal").contains("("+X+")("+Y+")("+Z+")"))) {return;}

	    	List<String> l = plugin.getParkourConfig().getStringList("goal");
	    	l.remove("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("goal", l);
	    	plugin.saveParkourConfig();
	    	player.sendMessage("The goal spot was broken."+ChatColor.GRAY+" ("+X+") ("+Y+") ("+Z+")");
	    	return;
	    }
	    if(!(plugin.getParkourConfig().getStringList("goal").contains("("+X+")("+Y+")("+Z+")"))){
	    	if(!(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")"))) {return;}

	    	List<String> l = plugin.getParkourConfig().getStringList("start");
	    	l.remove("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("start", l);
	    	String corr = "("+X+")("+Y+")("+Z+")";
	    	corr = corr.replace(".", "-");
	    	String name = plugin.getParkourConfig().getString(corr);
	    	plugin.getParkourConfig().set(corr,null);
	    	plugin.saveParkourConfig();
	    	boolean notdelete = false;
	    	Set<String> parents = plugin.getParkourConfig().getKeys(false);
	    	for(String s : parents) {
	    		if(s.startsWith("(")) {
	    			String course = plugin.getParkourConfig().getString(s);
	    			if(course.equals(name)) {
	    				notdelete = true;
	    				break;
	    			}
	    		}
	    	}
	    	if(!notdelete) {
	    		plugin.getDatabase().deleteCource(name);
	    	}
	    	player.sendMessage("The start spot was broken."+ChatColor.GRAY+" ("+X+") ("+Y+") ("+Z+")");
	    	return;
	    }
	    if((plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")"))
	    		&&(plugin.getParkourConfig().getStringList("goal").contains("("+X+")("+Y+")("+Z+")"))){
	    	List<String> l = plugin.getParkourConfig().getStringList("goal");
	    	l.remove("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("goal", l);
	    	List<String> ls = plugin.getParkourConfig().getStringList("start");
	    	ls.remove("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("start", ls);
	    	player.sendMessage("The goal spot was broken."+ChatColor.GRAY+" ("+X+") ("+Y+") ("+Z+")");
	    	player.sendMessage("The start spot was broken."+ChatColor.GRAY+" ("+X+") ("+Y+") ("+Z+")");
	    	plugin.saveParkourConfig();
	    	return;

	    }

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
	private static boolean hasItem(Player player,String name) {
		try {
			if(player.getInventory().getItemInMainHand().hasItemMeta()&&player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(name)) {
				return true;
			}
		if(player.getInventory().getItemInOffHand().hasItemMeta()&&player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(name)) {
              return true;
				}
		} catch(NoSuchMethodError error) {
			if(player.getInventory().getItemInHand().hasItemMeta()&&player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(name)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
	    Player player =  e.getPlayer();
	    ItemStack goal = createItem.getGoal();
	    Double x = e.getBlock().getLocation().getX();
	    Double y = e.getBlock().getLocation().getY();
	    Double z = e.getBlock().getLocation().getZ();
	    String X = String.valueOf(x);
	    String Y = String.valueOf(y);
	    String Z = String.valueOf(z);
	    if( hasItem(player,ChatColor.GOLD+"Set start") ) {
	    	String name = createItem.getParkourName(e.getItemInHand());
	    	if(!player.hasPermission("mp.item.use")) {
	    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
	    		return;
	    		}
	    	String corr = "("+X+")("+Y+")("+Z+")";
	    	corr = corr.replace(".", "-");
	    	if(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")")) {
	    		if(name!=null) {

	    			plugin.getParkourConfig().set(corr, name);
	    			boolean created = plugin.getDatabase().createCource(name, player);
		    		 player.sendMessage(String.valueOf(created));
	    		} else {
	    			plugin.getDatabase().deleteCource(name);
	    			plugin.getParkourConfig().set(corr, null);
	    		}
	    		e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    		player.sendMessage(ChatColor.GRAY+"Start spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    		return;
        }
	    	if(name!=null) {
	    		plugin.getDatabase().createCource(name, player);
    			plugin.getParkourConfig().set(corr, name);
    		} else {
    			plugin.getDatabase().deleteCource(name);
    			plugin.getParkourConfig().set(corr, null);
    		}
	    	e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    	player.sendMessage(ChatColor.GRAY+"Start spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    	List<String> l = plugin.getParkourConfig().getStringList("start");
	    	l.add("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("start", l);
	    	plugin.saveParkourConfig();
	    }
	    if( hasItem(player,goal) ) {
	    	if(!player.hasPermission("mp.item.use")) {
	    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
	    		return;
	    		}
	    	if(plugin.getParkourConfig().getStringList("goal").contains("("+X+")("+Y+")("+Z+")")) {
	    	e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    	player.sendMessage(ChatColor.GRAY+"Goal spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    	return;
	    	}
	    	e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    	player.sendMessage(ChatColor.GRAY+"Goal spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    	List<String> l = plugin.getParkourConfig().getStringList("goal");
	    	l.add("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("goal", l);
	    	plugin.saveParkourConfig();
	    }
	}
	@EventHandler
	public void InventoryEvent(InventoryClickEvent e) {
		 ItemStack start = createItem.getStart(null);
		 ItemStack goal = createItem.getGoal();
		 ItemStack checkpoint = createItem.getSign();
		 ItemStack close = createItem.getClose();
		 Player player = (Player) e.getWhoClicked();
		 if(e.getCurrentItem()==null) {return;}
		 if( e.getCurrentItem().equals(createItem.getShow())) {
			 if(player.getInventory().contains(createItem.getShow())) {
				 if(createItem.isEnable(player, e.getCurrentItem())) {
				Inventory i = player.getInventory();
				    i.remove(createItem.getShow());
					i.remove(createItem.getTeleporter());
					i.remove(createItem.getQuit());
					i.remove(createItem.getGuide());
				    i.remove(createItem.getConfigItem());
				    i.setItem(2,createItem.getQuit());
	                i.setItem(4,createItem.getGuide());
	                i.setItem(8,createItem.getConfigItem());
	                i.setItem(6,createItem.getUp());
				    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    	        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	    	            @Override
	    	            public void run() {
	    	            	i.setItem(0,createItem.getTeleporter());
	    	            }
	    	        }, 1L);
	    	        Sound sound= XSounds.BLOCK_CHEST_OPEN.parseSound();
	                player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
	                player.closeInventory();
				 }
		             e.setCancelled(true);
			 }
			}
		 String uuid = player.getUniqueId().toString();
		 if(plugin.getParkourConfig().getBoolean("isPlaying."+uuid)) {
			  ItemStack eye = createItem.getTeleporter();
			  ItemStack quit = createItem.getQuit();
			  ItemStack guide = createItem.getGuide();
			  ItemStack hide = createItem.getUp();
			  ItemStack config = createItem.getConfigItem();
			 if(e.getCurrentItem().equals(quit)&&player.getInventory().contains(quit)) {
				 if(createItem.isEnable(player, e.getCurrentItem())) {
				 createItem.quitMethod(player);
				 player.closeInventory();
				 }
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(guide)&&player.getInventory().contains(guide)) {
				 if(createItem.isEnable(player, e.getCurrentItem())) {
				 createItem.guideMethod(player);
				 player.closeInventory();
				 }
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(eye)&&player.getInventory().contains(eye)) {
				 if(createItem.isEnable(player, e.getCurrentItem())) {
				 createItem.teleportMethod(player);
				 player.closeInventory();
				 }
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(hide)&&player.getInventory().contains(hide)) {
				 if(createItem.isEnable(player, e.getCurrentItem())){
				 createItem.hideItemMethod(player);
				 player.closeInventory();
				 }
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(config)&&player.getInventory().contains(config)) {
				 player.closeInventory();
				 e.setCancelled(true);
			 }


		 }

		 if(InventoryMethod.getNameX(e).equals("Click to Get!")) {
			 if (e.getCurrentItem().equals(start)){
				 if(player.getInventory().contains(start)) {player.closeInventory();
	             e.setCancelled(true);
	             return;}
				 player.getInventory().addItem(start);
				 player.closeInventory();
	             e.setCancelled(true);
			 }
			 if (e.getCurrentItem().equals(goal)){
				 if(player.getInventory().contains(goal)) {player.closeInventory();
	             e.setCancelled(true);
	             return;}
				 player.getInventory().addItem(goal);
				 player.closeInventory();
	             e.setCancelled(true);
			 }
			 if (e.getCurrentItem().equals(checkpoint)){
				 if(player.getInventory().contains(checkpoint)) {player.closeInventory();
	             e.setCancelled(true);
	             return;}
				 InventoryMethod.addOneItem(player, checkpoint, createItem.getSignMeta());
				 player.closeInventory();
	             e.setCancelled(true);
			 }
			 if (e.getCurrentItem().equals(close)){
					player.closeInventory();
		            e.setCancelled(true);
				 }
           return;
		 }
		 if(InventoryMethod.getNameX(e).equals("Config")) {
			 if (e.getCurrentItem().equals(close)){
				player.closeInventory();
	            e.setCancelled(true);
			 }

			 if (e.getCurrentItem().equals(createItem.getToggleOff())){
				    if(player.hasMetadata("disableItem")) {
				    	List<MetadataValue>  meta = player.getMetadata("disableItem");
				    	player.removeMetadata("disableItem", plugin);
				    	for(MetadataValue m : meta) {
				    		if(m.getOwningPlugin().equals(plugin)) {
				    			@SuppressWarnings("unchecked")
								ArrayList<ItemStack> items = (ArrayList<ItemStack>)m.value();
				    			items.add(e.getInventory().getItem(e.getSlot()-9));
				    			player.setMetadata("disableItem", new FixedMetadataValue(plugin,items));


				    		}
				    	}
				    } else {
				    	ArrayList<ItemStack> items = new ArrayList<>();
		    			items.add(e.getInventory().getItem(e.getSlot()-9));
		    			player.setMetadata("disableItem", new FixedMetadataValue(plugin,items));

				    }
				    e.getInventory().setItem(e.getSlot(), createItem.getToggleOn());
				    player.updateInventory();
		            e.setCancelled(true);
		            return;
				 }
			 if (e.getCurrentItem().equals(createItem.getToggleOn())){
				    if(player.hasMetadata("disableItem")) {
				    	 if(player.hasMetadata("disableItem")) {
						    	List<MetadataValue>  meta = player.getMetadata("disableItem");
						    	for(MetadataValue m : meta) {
						    		if(m.getOwningPlugin().equals(plugin)) {
						    			@SuppressWarnings("unchecked")
										ArrayList<ItemStack> items = (ArrayList<ItemStack>)m.value();
						    			items.remove(e.getInventory().getItem(e.getSlot()-9));
						    			player.setMetadata("disableItem", new FixedMetadataValue(plugin,items));
						    		}
						    	}
						    } else {
						    	ArrayList<ItemStack> items = new ArrayList<>();
				    			player.setMetadata("disableItem", new FixedMetadataValue(plugin,items));
						    }
				    }
				    e.getInventory().setItem(e.getSlot(), createItem.getToggleOff());
		            player.updateInventory();
		            e.setCancelled(true);

		            return;
				 }

           return;
		 }
	}
	private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if((player.getLocation().getY()<0)&&plugin.getParkourConfig().getBoolean("isPlaying."+uuid)){
        	if(plugin.getConfig().getString(uuid)!= null) {
    		         World wt = plugin.getServer().getWorld(plugin.getConfig().getString(uuid+"."
    						+"world"));
    					        double xt = plugin.getConfig().getDouble(uuid+"."+"x");
    						    double yt = plugin.getConfig().getDouble(uuid+"."+"y");
    						    double zt = plugin.getConfig().getDouble(uuid+"."+"z");
    						    float yawt= (float) plugin.getConfig().getDouble(uuid+"."+"Yaw");
    						    float pitcht= (float) plugin.getConfig().getDouble(uuid+"."+"Pitch");
    						    player.teleport(new Location(wt,xt,yt,zt,yawt,pitcht));

    				      }
        }
        if (player.getVelocity().getY() > 0) {
            double jumpVelocity = (double) 0.42F;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += (double) ((float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F);
            }
            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
                     plugin.getConfig().set(uuid+".Jumping", true);
                     plugin.saveConfig();
                     BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
 	    	        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
 	    	            @Override
 	    	            public void run() {
 	    	            	plugin.getConfig().set(uuid+".Jumping", false);
 	                        plugin.saveConfig();
 	    	            }
 	    	        }, 10L);
                }
            }
        }
        if (player.isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }
    @EventHandler
    public void RightSign(PlayerInteractEvent e) {
    	Block b = e.getClickedBlock();
    	Player player =e.getPlayer();

    	if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    		if(XMaterial.SIGN_POST.parseMaterials().contains(b.getType())||XMaterial.WALL_SIGN.parseMaterials().contains(b.getType())) {
    			Sign s = (Sign)b.getState();
    			String[] line = s.getLines();
    			String l1 = line[0];
                if(l1.equalsIgnoreCase(ChatColor.GREEN+"["+ChatColor.GOLD+"CheckPoint"+ChatColor.GREEN+"]")) {
                	String uuid = player.getUniqueId().toString();
                	if(plugin.getConfig().getBoolean(uuid+".Jumping")||player.isFlying()) {
                		player.sendMessage(ChatColor.GRAY+"You can't register checkpoints in the air.");
                		return;
                		}
                	LocationMethod.saveLocation(player);
                    player.sendMessage("Checkpoint was registered.");
                    Sound sound= XSounds.BLOCK_NOTE_BLOCK_BELL.parseSound();
                    player.playSound(player.getLocation(), sound, 3.0f, 2.0f);
                }
    		}
    	}
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		boolean Join = plugin.getWorldConfig().getBoolean("DisableJoinMessage");
		if(Join)
		e.setJoinMessage(ChatColor.GOLD+""+ChatColor.BOLD+ChatColor.UNDERLINE+"Welcome, " + e.getPlayer().getName() + "!");
	}
	@EventHandler
    public void SignChange(SignChangeEvent e) {
    	if(e.getPlayer().hasPermission("mp.sign")) {
    	String[] line=e.getLines();
    	String l1=line[0];
    	if(l1.equalsIgnoreCase("[check]")) {
    		e.setLine(0,ChatColor.GREEN+"["+ChatColor.GOLD+"CheckPoint"+ChatColor.GREEN+"]");
            e.getPlayer().sendMessage(ChatColor.GREEN+"CheckPoint was created.");
    	 }else {}
    	}
    }
	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			try {
			if (!(e.getHand() == EquipmentSlot.HAND)) {
				if(e.getHand() == EquipmentSlot.OFF_HAND&&player.getInventory().getItemInOffHand().equals(createItem.getTeleporter())) {
					e.setCancelled(true);
				}
				return;
				}
			} catch(NoSuchMethodError error) {
				// less than 1.8.x
			}
			createItem.rightClick(player, e);
			return;

		 }
	}

	@EventHandler
	public void leftClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)||e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			if(hasItem(player,ChatColor.GOLD+"Set start")) {
             new AnvilGUI.Builder()
             .plugin(plugin)
             .text("Define Parkour Name")
             .title("Define Parkour Name")
             .onComplete((editor,text)->{
            	 ItemMeta meta = e.getItem().getItemMeta();
         	     meta.setDisplayName(ChatColor.GOLD+"Set start");
                 List<String> lorelist = new ArrayList<String>();
        			String lore0 = ChatColor.BLUE+"Right clicking, set start";
        			String lore1 = ChatColor.BLUE+"spot of parkours.";
        			lorelist.add(0,lore0); lorelist.add(1,lore1);
        			if(text != null) {
        				String lore2 = ChatColor.BLUE+"ParkourName: "+ChatColor.GREEN+text;
        				lorelist.add(2, lore2);
        				}
        			meta.setLore(lorelist);
        			e.getItem().setItemMeta(meta);

            	 editor.sendMessage(ChatColor.GREEN+"The parkour name has been set to "+ChatColor.YELLOW+text+ChatColor.GREEN+".");
            	 return AnvilGUI.Response.close();
            			 })
             .open(player);
             e.setCancelled(true);

			}

			return;

		 }
	}

}












