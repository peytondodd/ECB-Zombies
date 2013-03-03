package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitServerListener implements Listener {

	private final CoDMain plugin;
	
	public PlayerJoinQuitServerListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Game game = plugin.getGameManager().get(event.getPlayer());
		if (game != null)
			game.removePlayer(event.getPlayer());
	}
	
}
