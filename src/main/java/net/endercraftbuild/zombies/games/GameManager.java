package net.endercraftbuild.zombies.games;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.endercraftbuild.zombies.ZombiesMain;

import org.bukkit.configuration.ConfigurationSection;

public class GameManager {
	
	private final ZombiesMain plugin;
	private final Map<String, Game> games;
	
	public GameManager(ZombiesMain plugin) {
		this.plugin = plugin;
		this.games = new HashMap<String, Game>();
	}
	
	public void load() {
		for (Game game : getGames())
			game.stop();
		games.clear();
		
		@SuppressWarnings("unchecked")
		List<ConfigurationSection> gamesList = (List<ConfigurationSection>) plugin.getConfig().getList("games");
		for (ConfigurationSection gameSection : gamesList) {
			Game game = new Game();
			game.load(gameSection);
			put(game.getName(), game);
		}
	}
	
	public void save() {
		ConfigurationSection gamesSection = plugin.getConfig().createSection("games");
		for (Game game : getGames())
			game.save(gamesSection);
	}
	
	public Game get(String name) {
		return games.get(name);
	}
	
	public void put(String name, Game game) {
		if (games.containsKey(name))
			throw new IllegalArgumentException("A game with that name already exists.");
		games.put(name, game);
	}
	
	public Collection<Game> getGames() {
		return games.values();
	}
	
}
