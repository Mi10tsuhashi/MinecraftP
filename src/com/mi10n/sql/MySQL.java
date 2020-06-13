package com.mi10n.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mi10n.main.MinecraftP;


public class MySQL extends DataBase{
	private static String host = null;
	private static String port = null;
	private static String database = null;
	private static String user = null;
	private static String password = null;
	@Override
	protected Connection openConnection() {
		setConnection(()->{
		    MinecraftP plugin = MinecraftP.getPlugin(MinecraftP.class);
		    YamlConfiguration file = plugin.getSQLConfig();
			host=file.getString("Host");
	        port=file.getString("Port");
	        database=file.getString("Database");
	        user=file.getString("User");
	        password=file.getString("Password");
	        String URL = "jdbc:mysql://"+host+":"+port+"/"+database;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(URL, user, password);
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"Successfully connected to MySQL");
				return connection;
			} catch (ClassNotFoundException |SQLException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Failed to connect to MySQL.");
				return null;
			}
		});
		return null;
	}





}
