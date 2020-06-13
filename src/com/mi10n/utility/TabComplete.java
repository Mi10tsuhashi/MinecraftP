package com.mi10n.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;


public class TabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
      if(!(sender instanceof Player)) return null;
      if(cmd.getName().equalsIgnoreCase("want")) {
    	  if(!(args.length<2)) {return null;}
    	  final List<String> list = new ArrayList<String>(Arrays.asList("creator","teleporter","register","quit","guide","hideitem","showitem","config","cp"));
    	 return (args.length > 0) ? StringUtil.copyPartialMatches(args[0], list, new ArrayList<>()) : null;
      }
      if(cmd.getName().equalsIgnoreCase("loc")) {
    	  if(!(args.length<2)) {return null;}
    	  List<String> list = new ArrayList<String>(Arrays.asList("get","tp","save"));
    	 return (args.length > 0) ? StringUtil.copyPartialMatches(args[0], list, new ArrayList<>()) : null;
      }
      if(cmd.getName().equalsIgnoreCase("disable")) {
    	  if(args.length<2) {
    	  List<String> list = new ArrayList<String>(Arrays.asList("chest","join"));
    	  return (args.length > 0) ? StringUtil.copyPartialMatches(args[0], list, new ArrayList<>()) : null;
    	  }
    	  if(args.length>=2) {
        	  List<String> list = new ArrayList<String>(Arrays.asList("on","off"));
        	  return (args.length > 0) ? StringUtil.copyPartialMatches(args[1], list, new ArrayList<>()) : null;
          }

      }
		return null;
	}

}
