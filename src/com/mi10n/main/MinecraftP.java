package com.mi10n.main;







import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mi10n.Enents.Eventlis;
import com.mi10n.commands.DisableCommand;
import com.mi10n.commands.LocationCommand;
import com.mi10n.commands.ParkourCommand;
import com.mi10n.commands.WantCommand;
import com.mi10n.sql.DataBase;
import com.mi10n.sql.MySQL;
import com.mi10n.utility.InventoryMethod;
import com.mi10n.utility.LocationMethod;
import com.mi10n.utility.TabComplete;
import com.mi10n.utility.createItem;



public class MinecraftP extends JavaPlugin{
	static MinecraftP plugin;
	private File config  = null;
	private File world = null;
	private File inventory = null;
	private File parkour =null;
	private File sql =null;
	private DataBase database = null;
	YamlConfiguration worldConfig = new YamlConfiguration();
	YamlConfiguration inventoryConfig = new YamlConfiguration();
	YamlConfiguration parkourConfig = new YamlConfiguration();
	YamlConfiguration configfile = new YamlConfiguration();
	YamlConfiguration sqlConfig = new YamlConfiguration();
	@Override
	public void onEnable() {
		plugin=this;
		getCommand("want").setExecutor(new WantCommand());
		getCommand("rank").setExecutor(new ParkourCommand());
		getCommand("mybest").setExecutor(new ParkourCommand());
		getCommand("course").setExecutor(new ParkourCommand());
		getCommand("loc").setExecutor(new LocationCommand(this));
		getCommand("disable").setExecutor(new DisableCommand(this));
		getServer().getPluginManager().registerEvents(new Eventlis(this),this);
		getCommand("want").setTabCompleter(new TabComplete());
		getCommand("disable").setTabCompleter(new TabComplete());
		getCommand("loc").setTabCompleter(new TabComplete());
		new LocationMethod(this);
		new InventoryMethod(this);
		new createItem(this);
		world = new File(getDataFolder(),"world.yml");
		config  = new File(getDataFolder(),"config.yml");
		inventory = new File(getDataFolder(),"inventory.yml");
		parkour = new File(getDataFolder(),"parkour.yml");
		sql = new File(getDataFolder(),"sql.yml");
		mkdir();
        loadYamls();
        saveDefaultConfig();
        MySQL.initSQL();

	}
	private void mkdir() {
		if(!world.exists()) {
			saveResource("world.yml",false);
		}
		if(!sql.exists()) {
			saveResource("sql.yml",false);
		}
		if(!inventory.exists()) {
			saveResource("inventory.yml",false);
		}
		if(!parkour.exists()) {
			saveResource("parkour.yml",false);
		}
		if(!config.exists()) {
			saveResource("config.yml",false);
		}
	}
	private void loadYamls() {
		try {
			parkourConfig.load(parkour);
			inventoryConfig.load(inventory);
			worldConfig.load(world);
			sqlConfig.load(sql);
			getConfig().load(config);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}
	public YamlConfiguration getWorldConfig() {
	     return worldConfig;
	}
	public YamlConfiguration getSQLConfig() {
	     return sqlConfig;
	}
	public YamlConfiguration getInventoryConfig() {
	     return inventoryConfig;
	}
	public YamlConfiguration getParkourConfig() {
	     return parkourConfig;
	}
	public YamlConfiguration getConfig() {
	     return configfile;
	}
	public void saveWorldConfig() {
		try {
			worldConfig.save(world);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveSQLConfig() {
		try {
			sqlConfig.save(sql);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveConfig() {
		try {
			configfile.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveInventoryConfig() {
		try {
			inventoryConfig.save(inventory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveParkourConfig() {
		try {
			parkourConfig.save(parkour);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public DataBase getDatabase() {
		return database;
	}
	public void setDatabase(DataBase database) {
		this.database = database;
	}
   public static MinecraftP getPlugin() {
	   return plugin;
   }


	@Override
	public void onDisable() {
		this.database.closeConnection();
	}

}
