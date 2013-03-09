package net.endercraftbuild.cod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.pvp.CoDGame;
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

public class GameManager implements Listener {
	
	private final CoDMain plugin;
	private final GameTickTask gameTickTask;
	private final PerpetualNightTask perpetualNightTask;
	private final Map<String, Game> games;
	
	private final List<Game> activeGames;
	
	public GameManager(CoDMain plugin) {
		this.plugin = plugin;
		this.gameTickTask = new GameTickTask(plugin);
		this.perpetualNightTask = new PerpetualNightTask(plugin);
		this.games = new HashMap<String, Game>();
		this.activeGames = new ArrayList<Game>();
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
			case "CoDGame":
				game = new CoDGame(plugin);
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
		if (!games.containsKey(name))
			throw new IllegalArgumentException("There is no game with that name.");
		return games.get(name);
	}
	
	public Game get(Player player) {
		for (Game game : getGames())
			if (game.isInGame(player))
				return game;
		return null;
	}
	
	public void add(Game game) {
		if (games.containsKey(game.getName()))
			throw new IllegalArgumentException("A game with that name already exists.");
		games.put(game.getName(), game);
	}
	
	public void replace(Game game) {
		if (!games.containsKey(game.getName()))
			throw new IllegalArgumentException("There is no game with that name.");
		games.put(game.getName(), game);
	}
	
	public Collection<Game> getGames() {
		return games.values();
	}
	
	public Collection<String> getGameNames() {
		return games.keySet();
	}
	
	public Collection<Game> getActiveGames() {
		return activeGames;
	}
	
	public boolean isInGame(Player player) {
		return get(player) != null;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		activeGames.add(event.getGame());
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		activeGames.remove(event.getGame());
	}
	
}
