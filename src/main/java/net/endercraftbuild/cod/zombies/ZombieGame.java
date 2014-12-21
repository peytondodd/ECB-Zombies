package net.endercraftbuild.cod.zombies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.events.RoundStartEvent;
import net.endercraftbuild.cod.zombies.listeners.GameProgressListener;
import net.endercraftbuild.cod.zombies.listeners.PlayerDeathListener;
import net.endercraftbuild.cod.zombies.objects.Barrier;
import net.endercraftbuild.cod.zombies.objects.Door;
import net.endercraftbuild.cod.zombies.objects.GameEntity;
import net.endercraftbuild.cod.zombies.objects.GameWolf;
import net.endercraftbuild.cod.zombies.objects.GameZombie;
import net.endercraftbuild.cod.zombies.objects.LobbySign;
import net.endercraftbuild.cod.zombies.objects.Spawner;
import net.endercraftbuild.cod.zombies.tasks.FiresaleTask;
import net.endercraftbuild.cod.zombies.tasks.InstaKillTask;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ZombieGame extends Game {
	
	private Double zombieMultiplier;
	private Long maxWaves;
	private Location spawnLocation;
	private Location lobbyLocation;
	
	private final Map<Integer, String> waveMobs;
	private final List<Spawner> spawners;
	private final List<Barrier> barriers;
	private final List<Door> doors;
	private final List<LobbySign> signs;
	
	private BukkitRunnable pendingRoundStartTask;
	private Long currentWave;
	private Long waveKills;
	private final List<GameEntity> gameEntities;
    private boolean isAdvanced = false;
    private boolean isFireSale;
    private boolean isInstaKill;


	public ZombieGame(CoDMain plugin) {
		super(plugin);
		
		waveMobs = new HashMap<Integer, String>();
		spawners = new ArrayList<Spawner>();
		barriers = new ArrayList<Barrier>();
		doors = new ArrayList<Door>();
		signs = new ArrayList<LobbySign>();
		setCurrentWave(0L);
		setWaveKills(0L);
		gameEntities = new ArrayList<GameEntity>();
        isFireSale = false;
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
		this.setAdvanced(config.getBoolean("advanced"));

		setSpawnLocation(Utils.loadLocation(config));
		
		ConfigurationSection lobbyLocationSection = config.getConfigurationSection("lobby-location");
		if (lobbyLocationSection != null)
			setLobbyLocation(Utils.loadLocation(lobbyLocationSection));
		
		ConfigurationSection waveMobsSection = config.getConfigurationSection("wave-mobs");
		if (waveMobsSection != null)
			for (String wave : waveMobsSection.getKeys(false))
				waveMobs.put(Integer.parseInt(wave), waveMobsSection.getString(wave));
		
		ConfigurationSection spawnersSection = config.getConfigurationSection("spawners");
		for (String name : spawnersSection.getKeys(false)) {
			ConfigurationSection spawnerSection = spawnersSection.getConfigurationSection(name);
			Spawner spawner = new Spawner();
			spawner.load(spawnerSection);
			spawners.add(spawner);
		}
		
		ConfigurationSection signsSection = config.getConfigurationSection("signs");
		if (signsSection != null)
		for (String name : signsSection.getKeys(false)) {
			ConfigurationSection signSection = signsSection.getConfigurationSection(name);
			LobbySign sign = new LobbySign(getPlugin());
			sign.load(signSection);
			signs.add(sign);
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
        gameSection.set("advanced", isAdvanced());

		
		Utils.saveLocation(getSpawnLocation(), gameSection);
		
		if (getLobbyLocation() != null) {
			ConfigurationSection lobbyLocationSection = gameSection.createSection("lobby-location");
			Utils.saveLocation(getLobbyLocation(), lobbyLocationSection);
		}
		
		ConfigurationSection mobRoundsSection = gameSection.createSection("wave-mobs");
		for (Integer wave : waveMobs.keySet())
			mobRoundsSection.set(wave.toString(), waveMobs.get(wave));
		
		ConfigurationSection spawnersSection = gameSection.createSection("spawners");
		for (Spawner spawner : getSpawners())
			spawner.save(spawnersSection);
		
		ConfigurationSection barriersSection = gameSection.createSection("barriers");
		for (Barrier barrier : getBarriers())
			barrier.save(barriersSection);
		ConfigurationSection signsSection = gameSection.createSection("signs");
		for (LobbySign sign : getLobbySigns())
			sign.save(signsSection);
		
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
	
	@Override
	public void stop() {
		cancelRoundStartTask();
		clearRoundStartTask();
		super.stop();
        //call event first to display rounds survived
        setCurrentWave(0L);
	}



    public boolean isInstaKill() {
        return isInstaKill;
    }

    public void setInstaKill(boolean isInstaKill) {
        this.isInstaKill = isInstaKill;
    }

    public boolean isFireSaleActive() {
        return isFireSale;
    }

    public void setFireSale(boolean isFireSale) {
        this.isFireSale = isFireSale;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }
    public void setAdvanced(boolean isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public boolean toggleAdvanced() {
        return this.isAdvanced = !isAdvanced;
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
	
	public Location getLobbyLocation() {
		return lobbyLocation;
	}
	
	public void setLobbyLocation(Location lobbyLocation) {
		this.lobbyLocation = lobbyLocation;
	}
	
	public boolean isSpawningPaused() {
		return pendingRoundStartTask != null;
	}
	
	private void scheduleRoundStart() {
		cancelRoundStartTask();
		final ZombieGame game = this;
		this.pendingRoundStartTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (game.isActive()) {
					game.clearRoundStartTask();
					game.callEvent(new RoundStartEvent(game));
				}
			}
		};
		this.pendingRoundStartTask.runTaskLater(getPlugin(), 20L * 15L);
	}
	
	private void cancelRoundStartTask() {
		try {
			this.pendingRoundStartTask.cancel();
		} catch (NullPointerException | IllegalStateException e) {
			// no-op: wasn't scheduled
		}
	}
	
	private void clearRoundStartTask() {
		this.pendingRoundStartTask = null;
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
		if (waveKills >= getMaxEntityCount() && getLivingEntityCount() == 0)
			advanceWave();
	}
	
	public void advanceWave() {
		setCurrentWave(getCurrentWave() + 1);
		setWaveKills(0L);
		
		scheduleRoundStart();
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
	
	
	public void removeLobbySign(LobbySign lobbysign) {
		signs.remove(lobbysign);
	}
	public LobbySign findSign(Location location) {
		for (LobbySign sign : signs)
			if (sign.getLocation().equals(location))
				return sign;
		return null;
	}
	
	
	public void updateLobbySign() {

		for (LobbySign sign : signs)
			sign.updateSign();
			//sign.placeSign();
	}
	
	
	public List<LobbySign> getLobbySigns() {
		return signs;
	}
	
	public void addLobbySign(LobbySign lobbySign) {
		signs.add(lobbySign);
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
			if (gameEntity.getMob() == entity)
				return gameEntity;
		return null;
	}
	
	public void giveKit(Player player) {
		Utils.giveKit(player, getPlugin().getConfig());
	//	player.setAllowFlight(false);
		//player.setFlying(false);
		//player.removePotionEffect(PotionEffectType.INVISIBILITY);
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
		return !isSpawningPaused() && getRemainingEntityCount() > 0; 
	}
	
	public void spawnEntities() {
		if (!shouldSpawn())
			return;
		
		int spawnerCount = getSpawners().size();
		long spawnCount = Math.min(getMaxLivingEntityCount(), getRemainingEntityCount()) - getLivingEntityCount();
		while (spawnCount > 0) {
			Spawner spawner = getSpawners().get(getRandom(spawnerCount));
			if (spawner.isActive()) {
				GameEntity gameEntity = null;
				switch (getWaveMob()) {
				case "wolves":
					gameEntity = new GameWolf(this, spawner);
					break;
				
				default:
					gameEntity = new GameZombie(this, spawner);
					break;
				}
				gameEntity.spawn();
				gameEntities.add(gameEntity);
				spawnCount--;
			}
		}
	}
	
	public void despawnEntities() {
		Iterator<GameEntity> iterator = getGameEntities().iterator();
		while (iterator.hasNext()) {
			GameEntity gameEntity = iterator.next();
			gameEntity.despawn();
			iterator.remove();
		}
	}
	
	public void payPlayer(Player player, int amount) {
		Economy economy = getPlugin().getEconomy();
		
		if (economy.depositPlayer(player.getName(), amount).transactionSuccess()) {
			String balance = economy.format(economy.getBalance(player.getName()));

            if (player.hasPermission("cod.donor.1")) {
                amount += 5;
                String deposit = economy.format(amount);
                player.sendMessage(getPlugin().prefix + ChatColor.BLUE + "Gained: " + ChatColor.DARK_GREEN + deposit + ChatColor.BLUE + " (+5 Slayer)! Total: " + ChatColor.DARK_GREEN + balance + "!");
            } else if (player.hasPermission("cod.donor.2")) {
                amount += 8;
                String deposit = economy.format(amount);
                player.sendMessage(getPlugin().prefix + ChatColor.BLUE + "Gained: " + ChatColor.DARK_GREEN + deposit + ChatColor.BLUE + " (+8 Slayer+)! Total: " + ChatColor.DARK_GREEN + balance + "!");
            } else if (player.hasPermission("cod.donor.3")) {
                amount += 10;
                String deposit = economy.format(amount);
                player.sendMessage(getPlugin().prefix + ChatColor.BLUE + "Gained: " + ChatColor.DARK_GREEN + deposit + ChatColor.BLUE + " (+10 ???)! Total: " + ChatColor.DARK_GREEN + balance + "!");
            } else {
                String deposit = economy.format(amount);
                player.sendMessage(getPlugin().prefix + ChatColor.BLUE + "Gained: " + ChatColor.DARK_GREEN + deposit + ChatColor.BLUE + "! Total: " + ChatColor.DARK_GREEN + balance + "!");
            }

		}
	}
	
	public String getWaveMob() {
		for (Integer wave : waveMobs.keySet())
			if ((currentWave % wave) == 0)
				return waveMobs.get(wave);
		return "zombies";
	}
	
	public void setWaveMob(Integer wave, String mob) {
		if (mob == null)
			waveMobs.remove(wave);
		else
			waveMobs.put(wave, mob);
	}
	
	public boolean shouldSpawnRunner() {
		return getRandom(10) < 5;
	}

    public void updateScoreboard(Player player)
    {
        Economy economy = super.getPlugin().getEconomy();
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
        Score score_money = objective.getScore(ChatColor.GREEN + "Money:");
        Score remaining_zombies = objective.getScore(ChatColor.GREEN + "Zombies Left:");
        Score round = objective.getScore(ChatColor.GREEN + "Round:");
        Score health = objective.getScore(ChatColor.GREEN + "Health:");
        int balance = (int)economy.getBalance(player.getName());
        score_money.setScore(balance);
        remaining_zombies.setScore((int)getRemainingEntityCount());
        round.setScore(getCurrentWave().intValue());
        health.setScore((int)player.getHealth());
    }

    public Scoreboard createNewScoreboard(Player player)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Economy economy = super.getPlugin().getEconomy();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("zombies", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "Game: " + getName());
        Score score_money = objective.getScore(ChatColor.GREEN + "Money:");
        Score remaining_zombies = objective.getScore(ChatColor.GREEN + "Zombies Left:");
        Score round = objective.getScore(ChatColor.GREEN + "Round:");
        Score health = objective.getScore(ChatColor.GREEN + "Health:");
        int balance = (int)economy.getBalance(player.getName());
        score_money.setScore(balance);
        remaining_zombies.setScore((int)getRemainingEntityCount());
        round.setScore(getCurrentWave().intValue());
        health.setScore((int)player.getHealth());
        player.setScoreboard(board);
        return board;
    }

    public void startFiresale() {
        setFireSale(true);
        broadcast(getPlugin().prefix + ChatColor.BOLD + "FIRESALE active for 15 seconds! All boxes $25!");
        new FiresaleTask(this).runTaskLaterAsynchronously(getPlugin(), 300); //15s
        for(Player p : getPlayers()) {
            getPlugin().sendFloatingText(p, ChatColor.GREEN.toString() + ChatColor.BOLD + "FIRESALE Active!", ChatColor.YELLOW + "All boxes $25 for 15 seconds!");
        }
    }

    public void startInstaKill() {
        setInstaKill(true);
        broadcast(getPlugin().prefix + ChatColor.BOLD + "INSTA-KILL active for 30 seconds!");
        new InstaKillTask(this).runTaskLaterAsynchronously(getPlugin(), 600); //30 s
        for(Player p : getPlayers()) {
            getPlugin().sendFloatingText(p, ChatColor.GREEN.toString() + ChatColor.BOLD + "INSTA-KILL Active!", ChatColor.YELLOW + "All zombies insta-kill for 30 seconds!");
        }
    }


    public void randomPerkDrop() {
        int val = getRandom(450);
        if (val == 1) {
            startFiresale();
        } else if (val == 2) {
            startInstaKill();
        }
    }

}
