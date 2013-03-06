package net.endercraftbuild.cod.zombies.listeners.admin;

import java.util.Set;
import java.util.TreeSet;

import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Door;
import net.endercraftbuild.cod.zombies.objects.Spawner;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class LinkListener extends AdminListener {
	
	private Set<Door> doors;
	private Set<Spawner> spawners;

	public LinkListener(ZombieGame game, Player player) {
		super(game, player);
		this.doors = new TreeSet<Door>();
		this.spawners = new TreeSet<Spawner>();
		game.openBarriers();
		game.showDoors();
		game.showSpawners();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		if (isExitEvent(event)) {
			game.rebuildBarriers();
			game.hideDoors();
			game.hideSpawners();
			game.unregisterListener(this);
			
			for (Door linkDoor : doors)
				for (Spawner linkSpawner : spawners)
					linkDoor.addSpawner(linkSpawner);
			player.sendMessage(ChatColor.GREEN + "Linked " + doors.size() + " door(s) to " + spawners.size() + " spawner(s).");
			
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			return;
		}
		
		Block block = event.getClickedBlock();
		Door door = block != null ? game.findDoor(block.getLocation()) : null;
		Spawner spawner = block != null ? game.findSpawner(block.getLocation()) : null;
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (door != null) {
				doors.add(door);
				door.hide();
				player.sendMessage(ChatColor.GREEN + "Door link prepared.");
			} else if (spawner != null) {
				spawners.add(spawner);
				spawner.hide();
				player.sendMessage(ChatColor.GREEN + "Spawner link prepared.");
			} else {
				player.sendMessage(ChatColor.RED + "That is not a door or a spawner.");
			}
			break;
			
		default:
			break;
		}
	}
	
}
