package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.zombies.Door;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Wool;

public class DoorAdminListener implements Listener {
	
	private final ZombieGame game;
	private final Player player;

	public DoorAdminListener(ZombieGame game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		Block block = event.getClickedBlock();
		Door door = null;
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (block.getType() == Material.WOOL &&
					(((Wool) block).getColor() == DyeColor.CYAN || ((Wool) block).getColor() == DyeColor.ORANGE)) {
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
			if (block.getType() != Material.WOOL || ((Wool) block).getColor() != DyeColor.CYAN) {
				player.sendMessage(ChatColor.RED + "That is not a door.");
				return;
			}
			
			door = game.findDoor(block.getLocation());
			
			if (door == null) {
				player.sendMessage(ChatColor.RED + "That is not a door, just a piece of cyan wool you left laying around.");
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
