package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Spawner extends SerializableGameObject {
	
	private Location location;
	
	private boolean isLinked;
	private boolean isActive;
	
	public Spawner() {
		this.isLinked = false;
		this.isActive = false;
	}
	
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
		Wool wool = new Wool();
		wool.setColor(DyeColor.RED);
		location.getBlock().setType(wool.getItemType());
		location.getBlock().setData(wool.getData());
	}
	
	public void hide() {
		location.getBlock().setType(Material.AIR);
	}
	
}
