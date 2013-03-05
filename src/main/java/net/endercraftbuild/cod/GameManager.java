package net.endercraftbuild.cod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.pvp.CoDGame;
import net.endercraftbuild.cod.tasks.GameTickTask;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.CoDMain;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameManager implements Listener {
	
	private final CoDMain plugin;
	private final GameTickTask gameTickTask;
	private final Map<String, Game> games;
	
	private final List<Game> activeGames;
	
	public GameManager(CoDMain plugin) {
		this.plugin = plugin;
		this.gameTickTask = new GameTickTask(plugin);
		this.games = new HashMap<String, Game>();
		this.activeGames = new ArrayList<Game>();
	}
	
	public void load() {
		for (Game game : getActiveGames())
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
	
	public Collection<Game> getActiveGames() {
		return activeGames;
	}
	
	public boolean isInGame(Player player) {
		return get(player) != null;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		activeGames.add(event.getGame());
		if (activeGames.size() == 1)
			gameTickTask.start();
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		activeGames.remove(event.getGame());
		if (activeGames.isEmpty())
			gameTickTask.stop();
	}
	
}
