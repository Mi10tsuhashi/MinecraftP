package com.mi10n.commands;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mi10n.main.MinecraftP;
import com.mi10n.sql.DataBase;
import com.mi10n.utility.Time;

import net.md_5.bungee.api.ChatColor;

public class ParkourCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		MinecraftP plugin = JavaPlugin.getPlugin(MinecraftP.class);
        if(sender instanceof Player) {
        	player = (Player)sender;
        	switch(command.getName()) {
        	case "rank":
        		if(args.length==0) {player.sendMessage("Please specify the course name");return true;}


                plugin.getDatabase().executeQuery("SELECT uuid , time FROM MinecraftPtime WHERE courseId="
        		+plugin.getDatabase().getCourseId(args[0])+" ORDER BY time;",player,
        		(resultset,p)->{
        			try {
        				int i = 1;
						while(resultset.next()) {
                        String recorder = JavaPlugin.getPlugin(MinecraftP.class).getServer().getOfflinePlayer(
                        		DataBase.BytetoUUID(resultset.getBytes("uuid"))).getName();
                        String time = Time.format(resultset.getLong("time"));
                        p.sendMessage(ChatColor.GREEN+String.valueOf(i)+ChatColor.GRAY+". "+ChatColor.GOLD+recorder+ChatColor.GRAY+" : "+ChatColor.AQUA+time);
                        i++;
						}
						if(i==1) {
							p.sendMessage("Course does not exist or time does not exist.");
							return false;
						}
						return true;
					} catch (SQLException e) {
						e.printStackTrace();
					}
        			return false;
        			});
        		break;
        	case "mybest":
        		if(args.length==0) {
        			plugin.getDatabase().sendPlayedList(player, (result,p)->{
                           try {
                        	   int i = 1;
							while(result.next()) {
								String name = JavaPlugin.getPlugin(MinecraftP.class).getDatabase().getCourseName(result.getInt("courseId"));
								String time = Time.format(result.getLong("time"));
								if(!name.equals("null")) {
								p.sendMessage(ChatColor.GOLD+name+ChatColor.GRAY+" : "+ChatColor.AQUA+time);
								}
								i++;
							   }
							if(i==1) {
								p.sendMessage(ChatColor.GRAY+"Course does not exist or your record is missing.");
							}
						} catch (SQLException e) {
							e.printStackTrace();
							p.sendMessage(ChatColor.GRAY+"Course does not exist or your record is missing.");
						}

        			});
        			return true;
        			}
        		//TODO
        		long time = plugin.getDatabase().getBest(player, args[0]);
        		if(time==-1) {
                    player.sendMessage(ChatColor.GRAY+"Course does not exist or your record is missing.");
        		}else {
        			player.sendMessage(ChatColor.GREEN+player.getName()+ChatColor.GRAY+": "+ChatColor.GOLD+args[0]+ChatColor.GRAY+" : "+ChatColor.AQUA+Time.format(time));
        		}
        		break;
        	case "course":
        		if(args.length==0||args[0].equalsIgnoreCase("info")) {
        			plugin = JavaPlugin.getPlugin(MinecraftP.class);
        			if(plugin.getParkourConfig().getBoolean("isPlaying."+player.getUniqueId().toString())) {
        				String name =plugin.getParkourConfig().getString(player.getUniqueId().toString()+".parkour");
        			    Integer courseid = plugin.getDatabase().getCourseId(name);
        			    plugin.getDatabase().executeQuery("SELECT uuid, created FROM MinecraftPcourse WHERE courseId="+courseid+";", player, (result,p)->{
        			    	try {
        			    		if(result.next()) {
								String createdtime =null;
								Timestamp timestamp=null;
								switch(JavaPlugin.getPlugin(MinecraftP.class).getDatabase().type) {
								case MySQL:
									timestamp=result.getTimestamp("created");
									break;
								case SQLite:
									createdtime =result.getString("created");
									break;
									default:
										break;
								}
								String author = JavaPlugin.getPlugin(MinecraftP.class).getServer().getOfflinePlayer(DataBase.BytetoUUID(result.getBytes("uuid"))).getName();
								p.sendMessage(ChatColor.GOLD+"Course name"+ChatColor.GRAY+": "+ChatColor.WHITE+name);
								p.sendMessage(ChatColor.GOLD+"Author"+ChatColor.GRAY+": "+ChatColor.WHITE+author);
								switch(JavaPlugin.getPlugin(MinecraftP.class).getDatabase().type) {
								case MySQL:
									p.sendMessage(ChatColor.GOLD+"Created date"+ChatColor.GRAY+": "+ChatColor.WHITE+timestamp);
									break;
								case SQLite:
									p.sendMessage(ChatColor.GOLD+"Created date"+ChatColor.GRAY+": "+ChatColor.WHITE+createdtime);
									break;
									default:
										break;
								}
								}

							} catch (SQLException e) {
								e.printStackTrace();
							}
        			    	return false;});
        			}else {
        				player.sendMessage(ChatColor.GRAY+"You are not playing parkour now.");
        			}
        		}
        		if(args.length!=0&&args[0].equalsIgnoreCase("list")) {
                    plugin.getDatabase().executeQuery("SELECT name FROM MinecraftPcourse", player, (result,p)->{
                    	try {
                    		StringBuilder builder = new StringBuilder();
							while(result.next()) {
								builder.append(ChatColor.AQUA+result.getString("name"));
								builder.append(ChatColor.GRAY+", "+ChatColor.RESET);
							}
							if(builder.toString()==null || builder.toString().length()==0) {
								p.sendMessage(ChatColor.GRAY+"Course does not exist or your record is missing.");
							} else {
								p.sendMessage(builder.toString().substring(0, builder.toString().length()-6));
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
                    	return false;});
        		}
                break;

            default:
            	break;
        	}
        }
		return true;
	}

}
