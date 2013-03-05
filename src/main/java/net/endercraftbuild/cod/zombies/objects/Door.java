package net.endercraftbuild.cod.zombies.objects;

import java.util.Set;
import java.util.TreeSet;

import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class Door extends SerializableGameObject {
	
	public static final DyeColor COLOR = DyeColor.BLUE;
	
	private Location location;
	private Integer lowerTypeId;
	private byte lowerData;
	private Integer upperTypeId;
	private byte upperData;
	
	private Set<Spawner> spawners;
	
	public Door() {
		super();
		spawners = new TreeSet<Spawner>();
	}
	
	@Override
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		location = Utils.loadLocation(config);
		lowerTypeId = config.getInt("lowerTypeId");
		lowerData = config.getByteList("lowerData").get(0);
		upperTypeId = config.getInt("upperTypeId");
		upperData = config.getByteList("upperData").get(0);
		
		close();
		
		return config;
	}
	
	@Override
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection doorSection = super.save(parent);
		
		Utils.saveLocation(location, doorSection);
		doorSection.set("lowerTypeId", lowerTypeId);
		doorSection.set("lowerData", lowerData);
		doorSection.set("upperTypeId", upperTypeId);
		doorSection.set("upperData", upperData);

		return doorSection;
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
		close();
	}
	
	public void open() {
		getLowerBlock().setType(Material.AIR);
		getUpperBlock().setType(Material.AIR);
	}
	
	public void close() {
		getLowerBlock().setTypeId(lowerTypeId);
		getLowerBlock().setData(lowerData);
		getUpperBlock().setTypeId(upperTypeId);
		getUpperBlock().setData(upperData);
	}
	
	public Set<Spawner> getSpawners() {
		return spawners;
	}
	
	public void addSpawner(Spawner spawner) {
		spawner.setIsLinked(true);
		spawners.add(spawner);
	}
	
	public void removeSpawner(Spawner spawner) {
		spawner.setIsLinked(false);
		spawners.remove(spawner);
	}
	
	public void clearSpawners() {
		for (Spawner spawner : spawners)
			spawner.setIsLinked(false);
		spawners.clear();
	}
	
	public void activateSpawners() {
		for (Spawner spawner : spawners)
			spawner.activate();
	}
	
}
