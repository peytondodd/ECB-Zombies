package net.endercraftbuild.cod.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	//Temp methods
	public static ArrayList<String> PlayingZ = new ArrayList<String>();
	public static ArrayList<String> PlayingPvP = new ArrayList<String>();
	public static ArrayList<String> ISA = new ArrayList<String>();
	public static ArrayList<String> Mercs = new ArrayList<String>();
	public static ArrayList<String> Out = new ArrayList<String>();
	
	
	public static boolean isInGameZ(Player player) {
		String playerName = player.getName();
		if(PlayingZ.contains(playerName)) {
			return true;
		}
		else
			return false;
	}
	public static void setInGameZ(Player player, boolean bool) {
		String playerName = player.getName();
		if(isInGameZ(player)== false)
		{
			if(bool == true)
				PlayingZ.add(playerName);
			else
				if(isInGameZ(player) == true)
				{
					if(bool == false)
					{
						PlayingZ.remove(playerName);
					}
				}
		}
	}
	public static boolean isInGamePvP(Player player) {
		String playerName = player.getName();
		if(PlayingZ.contains(playerName)) {
			return true;
		}
		else
			return false;
	}
	public static void setInGamePvP(Player player, boolean bool, ArrayList<String> team) {
		String playerName = player.getName();
		if(isInGamePvP(player)== false)
		{
			if(bool == true)
				PlayingPvP.add(playerName);
			else
				if(isInGamePvP(player) == true)
				{
					if(bool == false)
					{
						//teleport to spawn
						PlayingPvP.remove(playerName);
					}
				}
		}
	}
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
}







