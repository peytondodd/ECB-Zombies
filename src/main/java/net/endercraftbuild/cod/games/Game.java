package net.endercraftbuild.cod.games;

import java.util.ArrayList;
import java.util.List;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Game {
	
	private final CoDMain plugin;
	private final List<Listener> listeners;
	
	private String name;
	private Long mininumPlayers;
	private Long maximumPlayers;
	
	private boolean isActive;
	
	public Game(CoDMain plugin) {
		this.plugin = plugin;
		this.listeners = new ArrayList<Listener>();
	}
	
	public CoDMain getPlugin() {
		return plugin;
	}
	
	public String getType() {
		return this.getClass().getSimpleName();
	}
	
	public ConfigurationSection load(ConfigurationSection config) {
		this.setName(config.getName());
		this.setMininumPlayers(config.getLong("min-players"));
		this.setMaximumPlayers(config.getLong("max-players"));
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection gameSection = parent.createSection(getName());
		
		gameSection.set("min-players", getMininumPlayers());
		gameSection.set("max-players", getMaximumPlayers());
		
		return gameSection;
	}
	
	public String getName() {
		return name;
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

	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void start() {
		this.setActive(true);
		GameStartEvent event = new GameStartEvent();
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	public void stop() {
		this.setActive(false);
		GameEndEvent event = new GameEndEvent();
		Bukkit.getServer().getPluginManager().callEvent(event);
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
	
}
