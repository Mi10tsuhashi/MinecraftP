package command;



import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.MinecraftP;
import utility.LocationMethod;

public class LocationCommand implements CommandExecutor  {

	 MinecraftP plugin;

    public LocationCommand(MinecraftP instance) {
        plugin = instance;
    }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			double x = (player.getLocation().getX());
			double y = (player.getLocation().getY());
			double z = (player.getLocation().getZ());
			String world = player.getLocation().getWorld().getName();
			String uuid = player.getUniqueId().toString();
						if(args.length == 0) {
				player.sendMessage(ChatColor.YELLOW+"[ERROE] "+ChatColor.GRAY+"Plese enter something in the args.");
		    }
			else if(args[0].equalsIgnoreCase("get") ) {
				if(player.hasPermission("mp.loc.get")) {
			    player.sendMessage(ChatColor.AQUA+"Your location is..."+"\nworld: "+world+" x: "
			+x+" y: "+y+" z: "+z);
			    }
				else {
					player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.loc.get)");
				}
			}
			else if(args[0].equalsIgnoreCase("save")){
				if(player.hasPermission("mp.loc.save")) {
					LocationMethod.saveLocation(player);
                player.sendMessage("Location data was saved.");
				} else {
					player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have "
							+ "enough permissions.   (permission: mp.loc.save)");
				}
			}
			else if(args[0].equalsIgnoreCase("tp")) {
				if(player.hasPermission("mp.loc.tp")) {
				    if(plugin.getConfig().contains(uuid+".x")) {
				      World wt = plugin.getServer().getWorld(plugin.getConfig().getString(uuid+"."
				      +"world"));
			          double xt = plugin.getConfig().getDouble(uuid+"."+"x");
				      double yt = plugin.getConfig().getDouble(uuid+"."+"y");
				      double zt = plugin.getConfig().getDouble(uuid+"."+"z");
				      float yawt= (float) plugin.getConfig().getDouble(uuid+"."+"Yaw");
				      float pitcht= (float) plugin.getConfig().getDouble(uuid+"."+"Pitch");
				      player.teleport(new Location(wt,xt,yt,zt,yawt,pitcht));
				      player.sendMessage(ChatColor.GRAY+"Teleported.");

			     	  }
				    else if (!plugin.getConfig().contains(uuid+".x")) {
					player.sendMessage(ChatColor.YELLOW+"[ERROR] "+
				    ChatColor.GRAY+"There is not your data.");
				    }
			   	} else {
					player.sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"[ERROR]"+ChatColor.GRAY+"You don't have enough "
							+ "permissions.   (permission: mp.loc.tp)");
			        	}
			}
			else {
				player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Invalid argument.");
			}

		}
		return true;
	}

}
