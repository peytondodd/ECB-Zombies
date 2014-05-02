package net.endercraftbuild.cod.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	//functions for getting/setting names
	public static String getItemName(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		return im.getDisplayName();
	}

	public static ItemStack setItemName(ItemStack is, String str) {
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

	@SuppressWarnings("deprecation")
	public static void giveKit(Player player, ConfigurationSection config) {
		PlayerInventory inventory = player.getInventory();
		
		for (String kitName : config.getConfigurationSection("kits.zombies").getKeys(false)) {
			if (!player.hasPermission("cod.kits.zombies." + kitName))
				continue;
			
			List<Map<?, ?>> items = config.getMapList("kits.zombies." + kitName);
			for (Map<?, ?> item : items) {
				ItemStack itemStack = new ItemStack((Integer) item.get("id"), 1);
				
				if (item.containsKey("quantity"))
					itemStack.setAmount((Integer) item.get("quantity"));
				if (item.containsKey("name"))
					setItemName(itemStack, (String) item.get("name"));
				
				if (item.containsKey("wear"))
					wearItem(player, itemStack, (String) item.get("wear"));
				else
					inventory.addItem(itemStack);
			}
		}
		
		player.updateInventory();
	}
	
	
	public static void wearItem(Player player, ItemStack itemStack, String location) {
		switch (location) {
		case "chest":
			player.getInventory().setChestplate(itemStack);
			break;
		case "head":
			player.getInventory().setHelmet(itemStack);
			break;
		case "legs":
			player.getInventory().setLeggings(itemStack);
			break;
		case "feet":
			player.getInventory().setBoots(itemStack);
			break;
		}
	}
	
	public static boolean isGunPaP(ItemStack is) {
		return getItemName(is) != null && getItemName(is).contains("PaP");
	}
	
	public static Entity getDamager(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if (entity instanceof Projectile)
			return (Entity) ((Projectile) entity).getShooter();
		else
			return entity;
	}
	 
	public static ItemStack addGlow(ItemStack item){ 
		net.minecraft.server.v1_7_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null) tag = nmsStack.getTag();
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}
	
	
	public static String formatMessage(String message, Object... args) {
		return ChatColor.translateAlternateColorCodes('&', String.format(message, args));
	}

    public static List<Location> circle (Location location, Integer radius, Integer height, Boolean hollow, Boolean sphere, int y_offset) {
        List<Location> circleblocks = new ArrayList<Location>();
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();
        for (int x = centerX - radius; x <= centerX +radius; x++)
            for (int z = centerZ - radius; z <= centerZ +radius; z++)
                for (int y = (sphere ? centerY - radius : centerY); y < (sphere ? centerY + radius : centerY + height); y++) {
                    double dist = (centerX - x) * (centerX - x) + (centerZ - z) * (centerZ - z) + (sphere ? (centerY - y) * (centerY - y) : 0);
                    if (dist < radius*radius && !(hollow && dist < (radius-1)*(radius-1))) {
                        Location l = new Location(location.getWorld(), x, y + y_offset, z);
                        circleblocks.add(l);
                    }
                }
     
        return circleblocks;
    }
}
