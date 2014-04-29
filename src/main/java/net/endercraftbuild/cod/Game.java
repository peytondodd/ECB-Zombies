package net.endercraftbuild.cod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class Game {
	
	private final CoDMain plugin;
	
	private final List<Listener> listeners;
	private final List<Player> players;
	private final Random random;
	private String name;
	private Long minimumPlayers;
	private Long maximumPlayers;
	private boolean isEnabled;
	private boolean isPrivate; //Dont allow joining in progress
	
	private Permission permission;
	private boolean isActive;
	
	public Game(CoDMain plugin) {
		this.plugin = plugin;
		this.listeners = new ArrayList<Listener>();
		this.players = new ArrayList<Player>();
		this.random = new Random();
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
		this.setEnabled(config.getBoolean("enabled", true));
		this.setPrivate(config.getBoolean("private", true));
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection gameSection = parent.createSection(getName());
		
		gameSection.set("min-players", getMinimumPlayers());
		gameSection.set("max-players", getMaximumPlayers());
		gameSection.set("enabled", isEnabled());
		gameSection.set("private", isPrivate());
		return gameSection;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		if (this.permission != null)
			plugin.getServer().getPluginManager().removePermission(this.permission);
		this.permission = new Permission("cod.player.join." + name, PermissionDefault.TRUE);
		plugin.getServer().getPluginManager().addPermission(this.permission);
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
	
	public boolean isEnabled() {
		return isEnabled;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public boolean setPrivate(boolean isPrivate) {
		return this.isPrivate = isPrivate;		
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public boolean togglePrivate() {
		return this.isPrivate = !isPrivate;
	}
	
	public boolean toggleEnabled() {
		return this.isEnabled = !isEnabled;
	}
	
	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public Permission getPermission() {
		return permission;
	}
	
	public void start() {
		this.setActive(true);
		registerListeners();
		callEvent(new GameStartEvent(this));
	}
	
	public void stop() {
		if (!isActive())
			return;
		
		callEvent(new GameEndEvent(this));
		this.setActive(false);
		removePlayers();
		clearListeners();
	}
	
	public void registerListener(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		listeners.add(listener);
	}
	
	public abstract void registerListeners();
	
	public void unregisterListener(Listener listener) {
		HandlerList.unregisterAll(listener);
		listeners.remove(listener);
	}
	
	public void clearListeners() {
		for (Listener listener : listeners)
			HandlerList.unregisterAll(listener);
		listeners.clear();
	}
	
	public void callEvent(Event event) {
		plugin.getServer().getPluginManager().callEvent(event);
	}
	
	public boolean isInGame(Player player) {
		return players.contains(player);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		if (!isEnabled())
			throw new RuntimeException("This game is not enabled.");
		if(isPrivate() && isActive())
			throw new RuntimeException("You cannot join this game while it is running. (Private game - donate!)");
		if (!player.hasPermission(getPermission()))
			throw new RuntimeException("You do not have permission to join this game.");
		if (isInGame(player))
			throw new IllegalArgumentException(player.getName() + " is already in this game.");
		if (plugin.getGameManager().didRecentlyLeave(player))
			throw new RuntimeException("You must wait 30 seconds before joining a game again.");
		if (players.size() == maximumPlayers)
			throw new RuntimeException(getName() + " is currently full.");
		players.add(player);
		if (isActive())
			callEvent(new PlayerJoinEvent(player, this));
		else if (players.size() >= minimumPlayers)
			start();
		else if (!(players.size() >= minimumPlayers))
			player.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Added to queue! " + minimumPlayers + " needed for game to start!");
	}
	
	public void removePlayer(Player player) {
		if (!isInGame(player))
			throw new IllegalArgumentException(player.getName() + " is not in this game.");
		callEvent(new PlayerLeaveEvent(player, this));
		players.remove(player);
		if (isActive() && !isPrivate() && players.size() < minimumPlayers) 
			stop();
		else if(isPrivate() && isActive() && players.size() == 0) {
			stop();
			
		}
	}
	
	public void removePlayers() {
		Iterator<Player> iterator = players.iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			callEvent(new PlayerLeaveEvent(player, this));
			iterator.remove();
		}
		if (isActive())
			stop();
	}
	
	public int getRandom(int max) {
		return random.nextInt(max);
	}
	
	public void broadcast(String message) {
		for (Player player : players)
			player.sendMessage(message);
	}
	
	public void broadcastToAll(String message) {
		plugin.getServer().broadcastMessage(message);
	}
	
	public void healPlayers() {
		for (Player player : players)
			player.setHealth(player.getMaxHealth());
	}
	
}
