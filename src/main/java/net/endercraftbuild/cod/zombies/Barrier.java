package net.endercraftbuild.cod.zombies;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Barrier {
	
	private Location location;
	private Material material;
	
	public ConfigurationSection load(ConfigurationSection config) {
		setLocation(Utils.loadLocation(config));
		material = Material.getMaterial(config.getString("material"));
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection barrierSection = parent.createSection(String.valueOf(this.hashCode()));
		
		Utils.saveLocation(getLocation(), barrierSection);
		barrierSection.set("material", material.toString());
		
		return barrierSection;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void show() {
		location.getBlock().setType(Material.WOOL);
		((Wool) location.getBlock()).setColor(DyeColor.PINK);
	}
	
	public void hide() {
		location.getBlock().setType(material);
	}
	
	public void rebuild() {
		location.getBlock().setType(material);
	}
	
}
