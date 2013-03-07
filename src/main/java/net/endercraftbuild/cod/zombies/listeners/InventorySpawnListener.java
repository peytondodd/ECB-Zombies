package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventorySpawnListener implements Listener {

	private final CoDMain plugin;
	
	public InventorySpawnListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		reset(player);
		((ZombieGame) plugin.getGameManager().get(player)).giveKit(player);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveEvent event) {
		reset(event.getPlayer());
	}
	
	private void reset(Player player) {
		player.setExp(0);
		player.setLevel(0);
		player.getInventory().clear();
		plugin.getEconomy().withdrawPlayer(player.getName(), plugin.getEconomy().getBalance(player.getName()));
	}
		
}
