package net.endercraftbuild.cod.games;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.utils.DoorHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;



public class Game {
	
	private String name;
	private Long mininumPlayers;
	private Long maximumPlayers;
	private Double zombieMultiplier;
	private Long maxWaves;
	private Location spawnLocation;
	private DoorHandler DH;
	
	private boolean isActive;
	private Long currentWave;
	
	public Game() {
	}
	
	public void load(ConfigurationSection config) {
		this.setName(config.getName());
		this.setMininumPlayers(config.getLong("min-players"));
		this.setMaximumPlayers(config.getLong("max-players"));
		this.setZombieMultiplier(config.getDouble("zombie-multiplier"));
		this.setMaxWaves(config.getLong("max-waves"));
	}
	
	public void save(ConfigurationSection parent) {
		ConfigurationSection gameSection = parent.createSection(getName());
		gameSection.set("min-players", getMininumPlayers());
		gameSection.set("max-players", getMaximumPlayers());
		gameSection.set("zombie-multiplier", getZombieMultiplier());
		gameSection.set("max-waves", getMaxWaves());
	}
	
	public String getName() {
		return name;
	}
	public DoorHandler getDoorHandler() {
		return DH;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMininumPlayers() {
		return mininumPlayers;
	}

	public void setMininumPlayers(Long mininumPlayers) {
		this.mininumPlayers = mininumPlayers;
	}

	public Long getMaximumPlayers() {
		return maximumPlayers;
	}

	public void setMaximumPlayers(Long maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
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

	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void start() {
		this.setActive(true);
		DH.DoorChecker();
		GameStartEvent gsevent = new GameStartEvent();
		Bukkit.getServer().getPluginManager().callEvent(gsevent);
	}
	
	public void stop() {
		this.setActive(false);
		GameEndEvent geevent = new GameEndEvent();
		Bukkit.getServer().getPluginManager().callEvent(geevent);
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
		this.setCurrentWave(currentWave + 1);
		RoundAdvanceEvent roundevent = new RoundAdvanceEvent();
		Bukkit.getServer().getPluginManager().callEvent(roundevent);
	}

}
