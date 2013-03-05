package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Spawner extends SerializableGameObject {
	
	public static final DyeColor COLOR = DyeColor.RED; 
	
	private Location location;
	
	private boolean isLinked;
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
	
	public boolean isLinked() {
		return isLinked;
	}
	
	public void setIsLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}

	public boolean isActive() {
		return isActive;
	}

	public void activate() {
		this.isActive = true;
	}
	
	public void deactivate() {
		this.isActive = false;
	}
	
	public void show() {
		location.getBlock().setType(Material.WOOL);
		((Wool) location.getBlock()).setColor(COLOR);
	}
	
	public void hide() {
		location.getBlock().setType(Material.AIR);
	}
	
}
