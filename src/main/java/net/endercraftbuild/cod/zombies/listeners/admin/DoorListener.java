package net.endercraftbuild.cod.zombies.listeners.admin;

import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Door;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorListener extends AdminListener {
	
	public DoorListener(ZombieGame game, Player player) {
		super(game, player);
		game.showDoors();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		if (isExitEvent(event)) {
			game.hideDoors();
			game.unregisterListener(this);
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			return;
		}
		
		Block block = event.getClickedBlock();
		Door door = block != null ? game.findDoor(block.getLocation()) : null;
		
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
			
		default:
			break;
		}
	}

}
