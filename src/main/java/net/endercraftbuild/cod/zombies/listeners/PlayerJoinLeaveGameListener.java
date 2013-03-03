package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerJoinLeaveGameListener implements Listener {

	private final CoDMain plugin;
	
	public PlayerJoinLeaveGameListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		Player player = event.getPlayer();
		reset(player);
		player.teleport(game.getSpawnLocation());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerLeaveEvent event) {
		Player player = event.getPlayer();
		reset(player);
		player.teleport(player.getWorld().getSpawnLocation());
	}
	
	private void reset(Player player) {
		player.setExp(0);
		player.getInventory().clear();
		plugin.getEconomy().withdrawPlayer(player.getName(), plugin.getEconomy().getBalance(player.getName()));
	}
}
