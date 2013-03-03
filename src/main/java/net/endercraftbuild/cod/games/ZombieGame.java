package net.endercraftbuild.cod.games;

import java.util.List;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.Spawner;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class ZombieGame extends Game {

	private Double zombieMultiplier;
	private Long maxWaves;
	private Location spawnLocation;
	private List<Spawner> spawners;

	private Long currentWave;

	public ZombieGame(CoDMain plugin) {
		super(plugin);
	}
	
	@Override
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		this.setZombieMultiplier(config.getDouble("zombie-multiplier"));
		this.setMaxWaves(config.getLong("max-waves"));
		
		setSpawnLocation(Utils.loadLocation(config));
		
		@SuppressWarnings("unchecked")
		List<ConfigurationSection> spawnerList = (List<ConfigurationSection>) config.getList("spawners");

		for (ConfigurationSection spawnerSection : spawnerList) {
			Spawner spawner = new Spawner();
			spawner.load(spawnerSection);
			spawners.add(spawner);
		}
		
		return config;
	}
	
	@Override
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection gameSection = super.save(parent);
		
		gameSection.set("zombie-multiplier", getZombieMultiplier());
		gameSection.set("max-waves", getMaxWaves());
		
		Utils.saveLocation(getSpawnLocation(), gameSection);
		
		ConfigurationSection spawnersSection = gameSection.createSection("spawners");
		for (Spawner spawner : getSpawners())
			spawner.save(spawnersSection);
		
		return gameSection;
	}

	public Double getZombieMultiplier() {
		return zombieMultiplier;
	}

	public void setZombieMultiplier(Double zombieMultiplier) {
		this.zombieMultiplier = zombieMultiplier;
	}

	public Long getMaxWaves() {
		return maxWaves;
	}

	public void setMaxWaves(Long maxWaves) {
		this.maxWaves = maxWaves;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public Long getCurrentWave() {
		return currentWave;
	}

	public void setCurrentWave(Long currentWave) {
		this.currentWave = currentWave;
	}
	
	public void advanceWave() {
		this.setCurrentWave(getCurrentWave() + 1);
		RoundAdvanceEvent roundevent = new RoundAdvanceEvent();
		Bukkit.getServer().getPluginManager().callEvent(roundevent);
	}

	public List<Spawner> getSpawners() {
		return spawners;
	}
	
	public void addSpawner(Spawner spawner) {
		spawners.add(spawner);
	}
	
	public void removeSpawner(Spawner spawner) {
		spawners.remove(spawner);
	}
	
	public Spawner findSpawner(Location location) {
		for (Spawner spawner : spawners)
			if (spawner.getLocation().equals(location))
				return spawner;
		return null;
	}
	
	public void showSpawners() {
		for (Spawner spawner : spawners)
			spawner.show();
	}
	
	public void hideSpawners() {
		for (Spawner spawner : spawners)
			spawner.hide();
	}

}
