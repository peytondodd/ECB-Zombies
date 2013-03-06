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
		lowerData = (byte) config.getInt("lowerData");
		upperTypeId = config.getInt("upperTypeId");
		upperData = (byte) config.getInt("upperData");
		
		close();
		
		return config;
	}
	
	@Override
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection doorSection = super.save(parent);
		
		Utils.saveLocation(location, doorSection);
		doorSection.set("lowerTypeId", lowerTypeId);
		doorSection.set("lowerData", new Integer(lowerData));
		doorSection.set("upperTypeId", upperTypeId);
		doorSection.set("upperData", new Integer(upperData));

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
		Wool wool = new Wool();
		wool.setColor(DyeColor.BLUE);
		getLowerBlock().setType(wool.getItemType());
		getLowerBlock().setData(wool.getData());
		getUpperBlock().setType(wool.getItemType());
		getUpperBlock().setData(wool.getData());
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
