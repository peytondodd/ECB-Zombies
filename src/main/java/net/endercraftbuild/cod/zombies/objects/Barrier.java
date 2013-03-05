package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Barrier extends SerializableGameObject {
	
	public static final DyeColor COLOR = DyeColor.ORANGE;
	
	private static final int DURABILITY = 8;
	private static final int DAMAGE = 1;
	
	private Location location;
	private Integer lowerTypeId;
	private byte lowerData;
	private Integer upperTypeId;
	private byte upperData;
	
	private int durability;
	
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		location = Utils.loadLocation(config);
		lowerTypeId = config.getInt("lowerTypeId");
		lowerData = config.getByteList("lowerData").get(0);
		upperTypeId = config.getInt("upperTypeId");
		upperData = config.getByteList("upperData").get(0);
		
		rebuild();
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection barrierSection = super.save(parent);
		
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
		((Wool) getLowerBlock()).setColor(COLOR);
		getUpperBlock().setType(Material.WOOL);
		((Wool) getUpperBlock()).setColor(COLOR);
	}
	
	public void hide() {
		rebuild();
	}
	
	public void open() {
		getLowerBlock().setType(Material.AIR);
		getUpperBlock().setType(Material.AIR);
	}
	
	public void damage() {
		if (durability == 0)
			return;
		
		durability -= DAMAGE;
		
		if (durability == 0)
			getLowerBlock().setType(Material.AIR);
		else if (durability <= (DURABILITY * 0.75))
			getUpperBlock().setType(Material.AIR);
	}
	
	public void rebuild() {
		durability = DURABILITY;
		getLowerBlock().setTypeId(lowerTypeId);
		getLowerBlock().setData(lowerData);
		getUpperBlock().setTypeId(upperTypeId);
		getUpperBlock().setData(upperData);
	}
	
}
