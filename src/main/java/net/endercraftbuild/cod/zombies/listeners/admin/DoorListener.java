package net.endercraftbuild.cod.zombies.listeners.admin;

import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Door;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorListener implements Listener {
	
	private final ZombieGame game;
	private final Player player;

	public DoorListener(ZombieGame game, Player player) {
		this.game = game;
		this.player = player;
		game.showDoors();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		Block block = event.getClickedBlock();
		Door door = game.findDoor(block.getLocation());
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (door != null) {
				player.sendMessage(ChatColor.RED + "That is already a door.");
				return;
			}
			
			door = new Door();
			door.setLocation(block.getLocation());
			game.addDoor(door);
			door.show();
			player.sendMessage(ChatColor.GREEN + "Door added.");
			
			break;
			
		case RIGHT_CLICK_BLOCK:
			if (door == null) {
				player.sendMessage(ChatColor.RED + "That is not a door.");
				return;
			}
			
			door.hide();
			game.removeDoor(door);
			player.sendMessage(ChatColor.GREEN + "Door removed.");
			
			break;
			
		case RIGHT_CLICK_AIR:
			game.hideDoors();
			game.unregisterListener(this);
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			break;
			
		default:
			break;
		}
	}

}
