package command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.MinecraftP;


public class DisableCommand implements CommandExecutor{
	 MinecraftP plugin;

	    public DisableCommand(MinecraftP instance) {
	        plugin = instance;
	    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			World world = player.getLocation().getWorld();
			if(!player.hasPermission("mp.disable")&&!player.hasPermission("mp.disable.chest")&&!player.hasPermission("mp.disable.join")) {
				player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.disable.*)");
				return true;
				}
			if(args.length==0) {
				player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Type is nothing.To see the type, type / disable help");
		  }else if(args[0].equalsIgnoreCase("chest")) {
			  if(!player.hasPermission("mp.disable.chest")) {
				  player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.disable.chest)");
				  return true;
			  }
			  if(args.length < 2) {player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Value is nothing. Please type /disable chest [on/off]");return true;}
			switch(args[1].toLowerCase()) {

			case "on":
                          plugin.getWorldConfig().set("World:"+world+"."+"DisableChest",true);
                          plugin.saveWorldConfig();
                          player.sendMessage(ChatColor.GRAY+"Changed setting in this world."
                        		  +ChatColor.BLACK+"["+ChatColor.BLUE+"ON"+ChatColor.BLACK+"]");
                          break;

			case "off":
			              plugin.getWorldConfig().set("World:"+world+"."+"DisableChest",false);
			              player.sendMessage(ChatColor.GRAY+"Changed setting in this world."
                        		  +ChatColor.BLACK+"["+ChatColor.RED+"OFF"+ChatColor.BLACK+"]");
			              plugin.saveWorldConfig();
			              break;

			default: player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Invalid value. /disable chest [on/off]");
			              break;
			}
       	} else if(args[0].equalsIgnoreCase("help")) {
		     		player.sendMessage("type: help, chest, join ");
		     	}
       	else if(args[0].equalsIgnoreCase("join")) {
       		if(!player.hasPermission("mp.disable.join")) {
				  player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.disable.join)");
				  return true;
			  }
       		if(args.length < 2) {player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Please type /disable join [on/off]");return true;}
       		switch(args[1].toLowerCase()) {

			case "on":
			           	  player.sendMessage(ChatColor.GRAY+"Join message enabled."
              		  +ChatColor.BLACK+"["+ChatColor.BLUE+"ON"+ChatColor.BLACK+"]");
				          plugin.getWorldConfig().set("DisableJoinMessage",true);
				          plugin.saveWorldConfig();
                          break;

			case "off":
				          player.sendMessage(ChatColor.GRAY+"Join message invalidated."
              		  +ChatColor.BLACK+"["+ChatColor.RED+"OFF"+ChatColor.BLACK+"]");
				          plugin.getWorldConfig().set("DisableJoinMessage",false);
				          plugin.saveWorldConfig();
			              break;

			default: player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Invalid value. /disable join [on/off]");
			break;
			}
       	}
       	else {
       		player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Invalid type. To see all type, please type /disable help");}
			return true;
		}
		if(sender instanceof BlockCommandSender) {
			((BlockCommandSender) sender).getBlock().getLocation();
			return true;}
		return true;

	}

}
