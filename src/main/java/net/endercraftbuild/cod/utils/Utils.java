package net.endercraftbuild.cod.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	//Temp methods
	public static ArrayList<String> ISA = new ArrayList<String>();
	public static ArrayList<String> Mercs = new ArrayList<String>();
	public static ArrayList<String> Out = new ArrayList<String>();
	
	//functions for getting/setting names
	public static String getItemName(ItemStack is)
	{
		ItemMeta im = is.getItemMeta();
		return im.getDisplayName();
	}

	public static ItemStack setItemName(ItemStack is, String str)
	{
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(str);
		is.setItemMeta(im);
		return is;
	}
	
	public static ConfigurationSection saveLocation(Location location, ConfigurationSection config) {
		config.set("world", location.getWorld().getName());
		config.set("xPos", location.getX());
		config.set("yPos", location.getY());
		config.set("zPos", location.getZ());
		config.set("yaw", location.getYaw());
		config.set("pitch", location.getPitch());
		return config;
	}
	
	public static Location loadLocation(ConfigurationSection config) {
		World world = Bukkit.getServer().getWorld(config.getString("world"));
		double xPos = config.getDouble("xPos");
		double yPos = config.getDouble("yPos");
		double zPos = config.getDouble("zPos");
		float yaw   = (float) config.getDouble("yaw");
		float pitch = (float) config.getDouble("pitch");
		return new Location(world, xPos, yPos, zPos, yaw, pitch);
	}

	public static void giveKit(Player player, ConfigurationSection config) {
		PlayerInventory inventory = player.getInventory();
		
		for (String kitName : config.getConfigurationSection("kits.zombies").getKeys(false)) {
			ConfigurationSection kit = config.getConfigurationSection("kits.zombies." + kitName);
			if (player.hasPermission("cod." + kit.getCurrentPath())) {
				for (String itemId : kit.getKeys(false)) {
					ConfigurationSection kitItem = kit.getConfigurationSection(itemId);
					ItemStack item = new ItemStack(Integer.parseInt(itemId), kitItem.getInt("quantity", 1));
					if (kitItem.contains("name"))
						Utils.setItemName(item, kitItem.getString("name"));
					inventory.addItem(item);
				}
			}
		}
	}
	
}
