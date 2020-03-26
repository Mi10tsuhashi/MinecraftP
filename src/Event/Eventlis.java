package Event;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.Sets;

import main.MinecraftP;
import utility.InventoryMethod;
import utility.LocationMethod;
import utility.XMaterial;
import utility.XSounds;
import utility.createItem;







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
                 plugin.getParkourConfig().set("isPlaying."+uuid, true);
                 plugin.saveParkourConfig();
                 InventoryMethod.InventorySave(player);
                 player.getInventory().clear();
                 InventoryMethod.SetStartItem(player);
 				 String pN = player.getName();
 				 LocationMethod.saveLocation(player);
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
    	    	plugin.saveParkourConfig();
    	    	String pn = player.getName();
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
     	        Bukkit.broadcastMessage(ChatColor.AQUA+""+pn+" cleared Parkour! "
     	        		+ChatColor.GRAY+""+day+"d, "+hours+"h, "+minutes+"m, "+seconds+"."+Time+"s");
     	        }
     	       if(day==0&&hours!=0) {
     	    	  Bukkit.broadcastMessage(ChatColor.AQUA+""+pn+" cleared Parkour! "
         	        		+ChatColor.GRAY+""+hours+"h, "+minutes+"m, "+seconds+"."+Time+"s");
        	        }
     	      if(day==0&&hours==0&&minutes!=0) {
     	    	 Bukkit.broadcastMessage(ChatColor.AQUA+""+pn+" cleared Parkour! "
     	        		+ChatColor.GRAY+""+minutes+"m, "+seconds+"."+Time+"s");
       	        }
     	     if(day==0&&hours==0&&minutes==0) {
     	    	 Bukkit.broadcastMessage(ChatColor.AQUA+""+pn+" cleared Parkour! "
         	        		+ChatColor.GRAY+""+seconds+"."+Time+"s");
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
	    	plugin.saveParkourConfig();
	    	player.sendMessage("The start spot was broken."+ChatColor.GRAY+" ("+X+") ("+Y+") ("+Z+")");
	    	return;
	    }
	    if((plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")"))
	    		&&(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")"))){
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
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
	    Player player =  e.getPlayer();
	    ItemStack start = createItem.getStart();
	    ItemStack goal = createItem.getGoal();
	    Double x = e.getBlock().getLocation().getX();
	    Double y = e.getBlock().getLocation().getY();
	    Double z = e.getBlock().getLocation().getZ();
	    String X = String.valueOf(x);
	    String Y = String.valueOf(y);
	    String Z = String.valueOf(z);
	    if( player.getInventory().getItemInMainHand().equals(start)
				|| player.getInventory().getItemInOffHand().equals(start) ) {
	    	if(!player.hasPermission("mp.item.use")) {
	    		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item.use)");
	    		return;
	    		}
	    	if(plugin.getParkourConfig().getStringList("start").contains("("+X+")("+Y+")("+Z+")")) {
	    		e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    		player.sendMessage(ChatColor.GRAY+"Start spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    		return;
        }
	    	e.getBlock().setType(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial());
	    	player.sendMessage(ChatColor.GRAY+"Start spot was created."+" ("+X+") ("+Y+") ("+Z+")");
	    	List<String> l = plugin.getParkourConfig().getStringList("start");
	    	l.add("("+X+")("+Y+")("+Z+")");
	    	plugin.getParkourConfig().set("start", l);
	    	plugin.saveParkourConfig();
	    }
	    if( player.getInventory().getItemInMainHand().equals(goal)
				|| player.getInventory().getItemInOffHand().equals(goal) ) {
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
		 ItemStack start = createItem.getStart();
		 ItemStack goal = createItem.getGoal();
		 ItemStack checkpoint = createItem.getSign();
		 ItemStack close = createItem.getClose();
		 Player player = (Player) e.getWhoClicked();
		 if(e.getCurrentItem()==null) {return;}
		 if( e.getCurrentItem().equals(createItem.getShow())) {
			 if(player.getInventory().contains(createItem.getShow())) {
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
	    	        Sound sound= Sound.BLOCK_CHEST_OPEN;
	                player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
	                player.closeInventory();
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
				 createItem.quidMethod(player);
				 player.closeInventory();
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(guide)&&player.getInventory().contains(guide)) {
				 createItem.guideMethod(player);
				 player.closeInventory();
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(eye)&&player.getInventory().contains(eye)) {
				 createItem.teleportMethod(player);
				 player.closeInventory();
				 e.setCancelled(true);
			 }
			 if(e.getCurrentItem().equals(hide)&&player.getInventory().contains(hide)) {
				 createItem.hideItemMethod(player);
				 player.closeInventory();
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
				 player.getInventory().addItem(checkpoint);
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
                    Sound sound= XSounds.BLOCK_NOTE_BLOCK_BELL.parseSounds();
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
			if (!(e.getHand() == EquipmentSlot.HAND)) {
				if(e.getHand() == EquipmentSlot.OFF_HAND&&player.getInventory().getItemInOffHand().equals(createItem.getTeleporter())) {
					e.setCancelled(true);
				}
				return;
				}
			createItem.rightClick(player, e);
			return;

		 }
	}
}












