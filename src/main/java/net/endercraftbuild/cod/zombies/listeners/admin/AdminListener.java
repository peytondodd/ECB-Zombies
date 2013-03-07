package net.endercraftbuild.cod.zombies.listeners.admin;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class AdminListener implements Listener {
	
	protected final ZombieGame game;
	protected final Player player;

	public AdminListener(ZombieGame game, Player player) {
		this.game = game;
		this.player = player;
	}

	public ZombieGame getGame() {
		return game;
	}

	public Player getPlayer() {
		return player;
	}
	
	protected boolean isExitEvent(PlayerInteractEvent event) {
		return ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) &&
				event.hasItem() && event.getItem().getType() == Material.STICK);
	}
	
}
