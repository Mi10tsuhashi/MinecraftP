package utility;

import org.bukkit.entity.Player;

import main.MinecraftP;

public class LocationMethod {

	static MinecraftP plugin;
	public LocationMethod(MinecraftP instance) {
        plugin = instance;
    }

    public static void saveLocation(Player player) {
    	String uuid = player.getUniqueId().toString();
		double x = (player.getLocation().getX());
		double y = (player.getLocation().getY());
		double z = (player.getLocation().getZ());
		float yaw= (float) ((Math.floor((player.getLocation().getYaw())*1000))/1000);
		float pitch= (float) ((Math.floor((player.getLocation().getPitch())*1000))/1000);
		String world = player.getLocation().getWorld().getName();
		String pN = player.getName();
		plugin.getConfig().set(uuid+"."+"playername",pN);
		plugin.getConfig().set(uuid+"."+"x",x);
		plugin.getConfig().set(uuid+"."+"y",y);
		plugin.getConfig().set(uuid+"."+"z",z);
		plugin.getConfig().set(uuid+"."+"Yaw",yaw);
		plugin.getConfig().set(uuid+"."+"Pitch",pitch);
		plugin.getConfig().set(uuid+"."+"world",world);
		plugin.saveConfig();
		return;
    }

}
