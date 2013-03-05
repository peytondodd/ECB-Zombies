package net.endercraftbuild.cod;

import java.util.ArrayList;
import java.util.List;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game {
	
	private final CoDMain plugin;
	
	private final List<Listener> listeners;
	private final List<Player> players;
	
	private String name;
	private Long minimumPlayers;
	private Long maximumPlayers;
	
	private boolean isActive;
	
	public Game(CoDMain plugin) {
		this.plugin = plugin;
		this.listeners = new ArrayList<Listener>();
		this.players = new ArrayList<Player>();
	}
	
	public CoDMain getPlugin() {
		return plugin;
	}
	
	public String getType() {
		return this.getClass().getSimpleName();
	}
	
	public ConfigurationSection load(ConfigurationSection config) {
		this.setName(config.getName());
		this.setMinimumPlayers(config.getLong("min-players"));
		this.setMaximumPlayers(config.getLong("max-players"));
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection gameSection = parent.createSection(getName());
		
		gameSection.set("min-players", getMinimumPlayers());
		gameSection.set("max-players", getMaximumPlayers());
		
		return gameSection;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getMinimumPlayers() {
		return minimumPlayers;
	}

	public void setMinimumPlayers(Long mininumPlayers) {
		this.minimumPlayers = mininumPlayers;
	}

	public Long getMaximumPlayers() {
		return maximumPlayers;
	}

	public void setMaximumPlayers(Long maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}

	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void start() {
		this.setActive(true);
		plugin.getServer().getPluginManager().callEvent(new GameStartEvent(this));
	}
	
	public void stop() {
		this.setActive(false);
		plugin.getServer().getPluginManager().callEvent(new GameEndEvent(this));
	}
	
	public void registerListener(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		listeners.add(listener);
	}
	
	public void unregisterListener(Listener listener) {
		HandlerList.unregisterAll(listener);
		listeners.remove(listener);
	}
	
	public void clearListeners() {
		for (Listener listener : listeners)
			unregisterListener(listener);
	}
	
	public boolean isInGame(Player player) {
		return players.contains(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		if (isInGame(player))
			throw new IllegalArgumentException(player.getName() + " is already in this game.");
		if (players.size() == maximumPlayers)
			throw new RuntimeException(getName() + " is currently full");
		players.add(player);
		if (isActive())
			plugin.getServer().getPluginManager().callEvent(new PlayerJoinEvent(player, this));
		else if (players.size() >= minimumPlayers)
			start();
	}
	
	public void removePlayer(Player player) {
		if (!isInGame(player))
			throw new IllegalArgumentException(player.getName() + " is not in this game.");
		players.remove(player);
		plugin.getServer().getPluginManager().callEvent(new PlayerLeaveEvent(player, this));
		if (isActive() && players.size() < minimumPlayers)
			stop();
	}
	
}
