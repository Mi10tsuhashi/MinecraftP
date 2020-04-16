package command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import utility.InventoryMethod;
import utility.createItem;

public class WantCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(!player.hasPermission("mp.item")) {
			player.sendMessage(ChatColor.YELLOW+"[ERROR] "+
				     ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item)");
					 return true;}
	    if(args.length==0) {
			if(player.hasPermission("mp.item")) {
			player.getInventory().addItem(createItem.getTeleporter(),createItem.getRegister());
			return true;

			}else {
                 player.sendMessage(ChatColor.YELLOW+"[ERROR] "+
			     ChatColor.GRAY+"You don't have enough permissions.   (permission: mp.item)");
				 return true;
			       }
			}
	    switch(args[0].toLowerCase()) {
	    case "register":
	    	player.getInventory().addItem(createItem.getRegister());
	    	break;
	    case "teleporter":
	    	player.getInventory().addItem(createItem.getTeleporter());
	    	break;
	    case "creator":
	    	player.getInventory().addItem(createItem.getCeator());
	    	break;
	    case "quit":
	    	player.getInventory().addItem(createItem.getQuit());
	    	break;
	    case "guide":
	    	player.getInventory().addItem(createItem.getGuide());
	    	break;
	    case "config":
	    	player.getInventory().addItem(createItem.getConfigItem());
	    	break;
	    case "hideitem":
	    	player.getInventory().addItem(createItem.getUp());
	    	break;
	    case "showitem":
	    	player.getInventory().addItem(createItem.getShow());
	    	break;
	    case "cp":
	    	InventoryMethod.addOneItem(player, createItem.getSign(), createItem.getSignMeta());
	    	break;
	    default:
	    	player.sendMessage(ChatColor.YELLOW+"[ERROR] "+ChatColor.GRAY+"Invalid argument.");
	    	break;
	          }
		}

		return true;
	}

}
