package net.endercraftbuild.cod.zombies;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Barrier {
	
	private Location location;
	private Integer lowerTypeId;
	private byte lowerData;
	private Integer upperTypeId;
	private byte upperData;
	
	public ConfigurationSection load(ConfigurationSection config) {
		location = Utils.loadLocation(config);
		lowerTypeId = config.getInt("lowerTypeId");
		lowerData = config.getByteList("lowerData").get(0);
		upperTypeId = config.getInt("upperTypeId");
		upperData = config.getByteList("upperData").get(0);
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection barrierSection = parent.createSection(String.valueOf(this.hashCode()));
		
		Utils.saveLocation(location, barrierSection);
		barrierSection.set("lowerTypeId", lowerTypeId);
		barrierSection.set("lowerData", lowerData);
		barrierSection.set("upperTypeId", upperTypeId);
		barrierSection.set("upperData", upperData);
		
		return barrierSection;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		this.lowerTypeId = getLowerBlock().getTypeId();
		this.lowerData = getLowerBlock().getData();
		this.upperTypeId = getUpperBlock().getTypeId();
		this.upperData = getUpperBlock().getData();
	}
	
	private Block getLowerBlock() {
		return location.getBlock();
	}
	
	private Block getUpperBlock() {
		return getLowerBlock().getRelative(BlockFace.UP);
	}
	
	public void show() {
		getLowerBlock().setType(Material.WOOL);
		((Wool) getLowerBlock()).setColor(DyeColor.PINK);
		getUpperBlock().setType(Material.WOOL);
		((Wool) getUpperBlock()).setColor(DyeColor.PURPLE);
	}
	
	public void hide() {
		rebuild();
	}
	
	public void rebuild() {
		getLowerBlock().setTypeId(lowerTypeId);
		getLowerBlock().setData(lowerData);
		getUpperBlock().setTypeId(upperTypeId);
		getUpperBlock().setData(upperData);
	}
	
}
