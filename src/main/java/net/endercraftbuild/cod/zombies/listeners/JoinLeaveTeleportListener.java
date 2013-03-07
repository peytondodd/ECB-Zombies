package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class JoinLeaveTeleportListener implements Listener {

	@SuppressWarnings("unused")
	private final CoDMain plugin;
	
	public JoinLeaveTeleportListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) event.getGame();
		player.teleport(game.getSpawnLocation());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerLeaveEvent event) {
		Player player = event.getPlayer();
		player.teleport(player.getWorld().getSpawnLocation());
	}
	
}
