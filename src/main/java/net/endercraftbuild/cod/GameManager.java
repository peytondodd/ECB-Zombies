package net.endercraftbuild.cod;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.endercraftbuild.cod.pvp.CoDGame;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.CoDMain;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class GameManager {
	
	private final CoDMain plugin;
	private final Map<String, Game> games;
	
	public GameManager(CoDMain plugin) {
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
	
	public void save() {
		ConfigurationSection gamesSection = plugin.getConfig().createSection("games");
		for (Game game : getGames()) {
			ConfigurationSection gameSection = game.save(gamesSection);
			gameSection.set("type", game.getType());
		}
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
	
	public boolean isInGame(Player player) {
		return get(player) != null;
	}
	
}
