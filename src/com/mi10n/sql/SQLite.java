package com.mi10n.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SQLite extends DataBase {

	@Override
	protected Connection openConnection() {
		setConnection(()->{
			File folder = new File("plugins/MinecraftP/sqlite");
			 if (!folder.exists()) {
			      folder.mkdirs();
			    }
			File file = new File(folder,"minecraftp.db");
			 if (!file.exists()) {
			      try {
			        file.createNewFile();
			      } catch (IOException e) {
			    	  Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Unable to create database:"+e.getMessage());
			      }
			    }
	        String URL = "jdbc:sqlite:"+folder+"/"+"minecraftp.db";
			try {
				Class.forName("org.sqlite.JDBC");
				Connection connection = DriverManager.getConnection(URL);
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"Successfully connected to SQLite");
				return connection;
			} catch (ClassNotFoundException |SQLException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Failed to connect to SQLite.");
				e.printStackTrace();
				return null;
			}
		});
		return null;
	}

}
