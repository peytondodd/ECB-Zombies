package net.endercraftbuild.cod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.tasks.GameTickTask;
import net.endercraftbuild.cod.tasks.PerpetualNightTask;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.CoDMain;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager implements Listener {
	
	private final CoDMain plugin;
	private final GameTickTask gameTickTask;
	private final PerpetualNightTask perpetualNightTask;
	private final List<Game> games;
	
	private final List<Game> activeGames;
	private final Map<String, Game> recentPlayers;
	
	public GameManager(CoDMain plugin) {
		this.plugin = plugin;
		this.gameTickTask = new GameTickTask(plugin);
		this.perpetualNightTask = new PerpetualNightTask(plugin);
		this.games = new ArrayList<Game>();
		this.activeGames = new ArrayList<Game>();
		this.recentPlayers = new HashMap<String, Game>();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		gameTickTask.start();
		perpetualNightTask.start();
	}
	
	public void disable() {
		for (Game game : getActiveGames())
			game.stop();
		gameTickTask.stop();
		perpetualNightTask.stop();
	}
	
	public void load() throws IOException, InvalidConfigurationException {
		for (Game game : getActiveGames())
			game.stop();
		games.clear();
		
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(plugin.getDataFolder().getPath() + File.separatorChar + "games.yml");
		} catch (FileNotFoundException e) {
			plugin.getLogger().log(Level.WARNING, "Missing game configuration, have you setup any and saved them?");
			return;
		}
		
		ConfigurationSection gamesSection = config.getConfigurationSection("games");
		for (String name : gamesSection.getKeys(false)) {
			ConfigurationSection gameSection = gamesSection.getConfigurationSection(name);
			
			Game game = null;

			switch (gameSection.getString("type")) {
			case "ZombieGame":
				game = new ZombieGame(plugin);
				break;
			default:
				// TODO(mortu): handle unknown game type? error?
				break;
			}
			
			if (game != null) {
				game.load(gameSection);
				add(game);
			}
		}
	}
	
	public void save() throws IOException {
		YamlConfiguration config = new YamlConfiguration();
		
		ConfigurationSection gamesSection = config.createSection("games");
		for (Game game : getGames()) {
			ConfigurationSection gameSection = game.save(gamesSection);
			gameSection.set("type", game.getType());
		}
		
		config.save(plugin.getDataFolder().getPath() + File.separatorChar + "games.yml");
	}
	
	public Game get(String name) {
		for (Game game : getGames())
			if (game.getName().equalsIgnoreCase(name))
				return game;
		throw new IllegalArgumentException("There is no game with that name.");
	}
	
	public Game get(Player player) {
		for (Game game : getGames())
			if (game.isInGame(player))
				return game;
		return null;
	}
	
	public void add(Game game) {
		for (Game existingGame : getGames())
			if (existingGame.getName().equalsIgnoreCase(game.getName()))
				throw new IllegalArgumentException("A game with that name already exists.");
		games.add(game);
	}
	
	public void remove(Game game) {
		if (!games.contains(game))
			throw new IllegalArgumentException("That does not appear to be a registered game.");
		games.remove(game);
	}
	
	public Collection<Game> getGames() {
		return games;
	}
	
	public Collection<String> getGameNames() {
		List<String> names = new ArrayList<>();
		for (Game game : getGames())
			names.add(game.getName());
		return names;
	}
	
	public Collection<Game> getActiveGames() {
		return activeGames;
	}
	
	public boolean isInGame(Player player) {
		return get(player) != null;
	}
	
	public boolean didRecentlyLeave(Player player) {
		return recentPlayers.containsKey(player.getName());
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		activeGames.add(event.getGame());
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		activeGames.remove(event.getGame());
		Iterator<Game> iterator = recentPlayers.values().iterator();
		while (iterator.hasNext()) {
			Game game = iterator.next();
			if (event.getGame() != game)
				continue;
			iterator.remove();
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveEvent event) {
		final String name = event.getPlayer().getName();
		final Game game = event.getGame();
		
		recentPlayers.put(name, game);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (recentPlayers.get(name) == game)
					recentPlayers.remove(name);
			}
		}.runTaskLater(plugin, 20L * 30L);
	}
	
}
