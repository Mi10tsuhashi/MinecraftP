package main;







import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import Event.Eventlis;
import command.DisableCommand;
import command.LocationCommand;
import command.WantCommand;
import utility.InventoryMethod;
import utility.LocationMethod;
import utility.TabComplete;
import utility.createItem;



public class MinecraftP extends JavaPlugin{
	static MinecraftP plugin;
	private File config  = null;
	private File world = null;
	private File inventory = null;
	private File parkour =null;
	YamlConfiguration worldConfig = new YamlConfiguration();
	YamlConfiguration inventoryConfig = new YamlConfiguration();
	YamlConfiguration parkourConfig = new YamlConfiguration();
	YamlConfiguration configfile = new YamlConfiguration();
	@Override
	public void onEnable() {
		plugin=this;
		getCommand("want").setExecutor(new WantCommand());
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
		mkdir();
        loadYamls();
        saveDefaultConfig();

	}
	private void mkdir() {
		if(!world.exists()) {
			saveResource("world.yml",false);
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
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		try {
			inventoryConfig.load(inventory);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		try {
			worldConfig.load(world);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	    try {
			getConfig().load(config);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	public YamlConfiguration getWorldConfig() {
	     return worldConfig;
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

   public static MinecraftP getPlugin() {
	   return plugin;
   }


	@Override
	public void onDisable() {

	}

}
