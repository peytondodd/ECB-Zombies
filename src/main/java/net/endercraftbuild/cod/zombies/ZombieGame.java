package net.endercraftbuild.cod.zombies;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.events.SpawnGameEntityEvent;
import net.endercraftbuild.cod.zombies.listeners.GameProgressListener;
import net.endercraftbuild.cod.zombies.listeners.PlayerDeathListener;
import net.endercraftbuild.cod.zombies.objects.Barrier;
import net.endercraftbuild.cod.zombies.objects.Door;
import net.endercraftbuild.cod.zombies.objects.GameEntity;
import net.endercraftbuild.cod.zombies.objects.Spawner;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ZombieGame extends Game {
	
	private Double zombieMultiplier;
	private Long maxWaves;
	private Location spawnLocation;
	
	private final List<Spawner> spawners;
	private final List<Barrier> barriers;
	private final List<Door> doors;

	private Long currentWave;
	private final List<GameEntity> gameEntities;
	private Long waveKills;
	private Long waveDelayUntil;

	public ZombieGame(CoDMain plugin) {
		super(plugin);
		spawners = new ArrayList<Spawner>();
		barriers = new ArrayList<Barrier>();
		doors = new ArrayList<Door>();
		
		setCurrentWave(0L);
		gameEntities = new ArrayList<GameEntity>();
	}
	
	@Override
	public void registerListeners() {
		registerListener(new GameProgressListener(getPlugin(), this));
		registerListener(new PlayerDeathListener(getPlugin(), this));
	}
	
	@Override
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		this.setZombieMultiplier(config.getDouble("zombie-multiplier"));
		this.setMaxWaves(config.getLong("max-waves"));
		
		setSpawnLocation(Utils.loadLocation(config));
		
		ConfigurationSection spawnersSection = config.getConfigurationSection("spawners");
		for (String name : spawnersSection.getKeys(false)) {
			ConfigurationSection spawnerSection = spawnersSection.getConfigurationSection(name);
			Spawner spawner = new Spawner();
			spawner.load(spawnerSection);
			spawners.add(spawner);
		}
		
		ConfigurationSection barriersSection = config.getConfigurationSection("barriers");
		for (String name : barriersSection.getKeys(false)) {
			ConfigurationSection barrierSection = barriersSection.getConfigurationSection(name);
			Barrier barrier = new Barrier();
			barrier.load(barrierSection);
			barriers.add(barrier);
		}
		
		ConfigurationSection doorsSection = config.getConfigurationSection("doors");
		for (String name : doorsSection.getKeys(false)) {
			ConfigurationSection doorSection = doorsSection.getConfigurationSection(name);
			Door door = new Door();
			
			door.load(doorSection);
			
			List<String> spawnerLinks = doorSection.getStringList("spawners");
			for (String spawnerId : spawnerLinks)
				door.addSpawner(findSpawner(spawnerId));
			
			doors.add(door);
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
		
		ConfigurationSection barriersSection = gameSection.createSection("barriers");
		for (Barrier barrier : getBarriers())
			barrier.save(barriersSection);
		
		ConfigurationSection doorsSection = gameSection.createSection("doors");
		for (Door door : getDoors()) {
			ConfigurationSection doorSection = door.save(doorsSection);
			
			List<String> spawnerLinks = new ArrayList<String>();
			for (Spawner spawner : door.getSpawners())
				spawnerLinks.add(spawner.getId().toString());
			
			doorSection.set("spawners", spawnerLinks);
		}
		
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
	
	private void setWaveDelay() {
		this.waveDelayUntil = new Date().getTime() + 20 * 1000;
	}

	public Long getCurrentWave() {
		return currentWave;
	}

	public void setCurrentWave(Long currentWave) {
		this.currentWave = currentWave;
	}
	
	public Long getWaveKills() {
		return waveKills;
	}
	
	public void setWaveKills(Long waveKills) {
		this.waveKills = waveKills;
	}
	
	public void incrementWaveKills() {
		waveKills++;
		if (waveKills == getMaxEntityCount())
			advanceWave();
	}
	
	public void advanceWave() {
		setCurrentWave(getCurrentWave() + 1);
		setWaveKills(0L);
		setWaveDelay();
		callEvent(new RoundAdvanceEvent(this));
	}

	public List<Spawner> getSpawners() {
		return spawners;
	}
	
	public void addSpawner(Spawner spawner) {
		spawners.add(spawner);
	}
	
	public void removeSpawner(Spawner spawner) {
		for (Door door : doors)
			door.removeSpawner(spawner);
		spawners.remove(spawner);
	}
	
	public Spawner findSpawner(Location location) {
		for (Spawner spawner : spawners)
			if (spawner.getLocation().equals(location))
				return spawner;
		return null;
	}
	
	public Spawner findSpawner(String id) {
		for (Spawner spawner : spawners)
			if (spawner.getId().toString().equals(id))
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
	
	public void activateSpawners() {
		for (Spawner spawner : spawners)
			if (!spawner.isLinked())
				spawner.activate();
	}
	
	public void deactivateSpawners() {
		for (Spawner spawner : spawners)
			spawner.deactivate();
	}
	
	public List<Barrier> getBarriers() {
		return barriers;
	}
	
	public void addBarrier(Barrier barrier) {
		barriers.add(barrier);
	}
	
	public void removeBarrier(Barrier barrier) {
		barriers.remove(barrier);
	}
	
	public Barrier findBarrier(Location location) {
		for (Barrier barrier : barriers)
			if (barrier.getLocation().equals(location))
				return barrier;
		return null;
	}
	
	public void showBarriers() {
		for (Barrier barrier : barriers)
			barrier.show();
	}
	
	public void hideBarriers() {
		for (Barrier barrier : barriers)
			barrier.hide();
	}
	
	public void rebuildBarriers() {
		for (Barrier barrier : barriers)
			barrier.rebuild();
	}
	
	public void openBarriers() {
		for (Barrier barrier : barriers)
			barrier.open();
	}
	
	public void damageBarriers() {
		for (Barrier barrier : barriers)
			for (GameEntity gameEntity : gameEntities)
				if (gameEntity.near(barrier.getLocation())) {
					barrier.damage();
					break;
				}
	}
	
	
	public List<Door> getDoors() {
		return doors;
	}
	
	public void addDoor(Door door) {
		doors.add(door);
	}
	
	public void removeDoor(Door door) {
		doors.remove(door);
	}
	
	public Door findDoor(Location location) {
		for (Door door : doors)
			if (door.getLocation().equals(location))
				return door;
		return null;
	}
	
	public void showDoors() {
		for (Door door : doors)
			door.show();
	}
	
	public void hideDoors() {
		for (Door door : doors)
			door.hide();
	}
	
	public void closeDoors() {
		for (Door door : doors)
			door.close();
	}
	
	public List<GameEntity> getGameEntities() {
		return gameEntities;
	}
	
	public void addGameEntity(GameEntity gameEntity) {
		gameEntities.add(gameEntity);
	}
	
	public void removeGameEntity(GameEntity gameEntity) {
		gameEntities.remove(gameEntity);
	}
	
	public GameEntity findGameEntity(Entity entity) {
		for (GameEntity gameEntity : gameEntities)
			if (gameEntity.getEntity() == entity)
				return gameEntity;
		return null;
	}
	
	public void giveKit(Player player) {
		Utils.giveKit(player, getPlugin().getConfig());
	}
	
	public Long getMaxEntityCount() {
		return getMaxLivingEntityCount() * getCurrentWave();
	}
	
	public Long getMaxLivingEntityCount() {
		Long base = Math.max(getCurrentWave(), 4);
		Double multiplier = (getPlayers().size() - 1) * getZombieMultiplier();
		Double extra = base * multiplier;
		return base + extra.longValue();
	}
	
	public int getLivingEntityCount() {
		return getGameEntities().size();
	}
	
	public long getRemainingEntityCount() {
		return getMaxEntityCount() - getWaveKills();
	}
	
	private boolean shouldSpawn() {
		return (new Date()).getTime() > this.waveDelayUntil && getRemainingEntityCount() > 0; 
	}
	
	public void spawnEntities() {
		if (!shouldSpawn())
			return;
		
		int spawnerCount = getSpawners().size();
		long spawnCount = Math.min(getMaxLivingEntityCount(), getRemainingEntityCount()) - getLivingEntityCount();
		while (spawnCount > 0) {
			Spawner spawner = getSpawners().get(getRandom(spawnerCount));
			if (spawner.isActive()) {
				spawnCount--;
				callEvent(new SpawnGameEntityEvent(this, spawner));
			}
		}
	}
	
	public void payPlayer(Player player, int amount) {
		Economy economy = getPlugin().getEconomy();
		
		if (economy.depositPlayer(player.getName(), amount).transactionSuccess()) {
			String balance = economy.format(economy.getBalance(player.getName()));
			String deposit = economy.format(amount);
			player.sendMessage(getPlugin().prefix + ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + balance + ChatColor.GREEN + "! Gained: " + ChatColor.DARK_GREEN + deposit + "!");
		}
	}
	
}
