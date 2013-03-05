package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Spawner extends SerializableGameObject {
	
	private Location location;
	
	private boolean isActive;
	
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		location = Utils.loadLocation(config);
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection spawnerSection = super.save(parent);
		
		Utils.saveLocation(location, spawnerSection);
		
		return spawnerSection;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void show() {
		location.getBlock().setType(Material.WOOL);
		((Wool) location.getBlock()).setColor(DyeColor.RED);
	}
	
	public void hide() {
		location.getBlock().setType(Material.AIR);
	}
	
}
