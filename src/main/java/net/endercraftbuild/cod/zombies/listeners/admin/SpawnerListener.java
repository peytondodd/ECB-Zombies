package net.endercraftbuild.cod.zombies.listeners.admin;

import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Spawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpawnerListener extends AdminListener {
	
	public SpawnerListener(ZombieGame game, Player player) {
		super(game, player);
		game.showSpawners();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		if (isExitEvent(event)) {
			game.hideSpawners();
			game.unregisterListener(this);
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			return;
		}
		
		Block block = event.getClickedBlock();
		Spawner spawner = block != null ? game.findSpawner(block.getLocation()) : null;
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (spawner != null) {
				player.sendMessage(ChatColor.RED + "That is already a spawner.");
				return;
			}
			
			block = block.getRelative(BlockFace.UP);
			
			if (block.getType() != Material.AIR) {
				player.sendMessage(ChatColor.RED + "Cannot add spawner, no air at location.");
				return;
			}
			
			spawner = new Spawner();
			spawner.setLocation(block.getLocation());
			game.addSpawner(spawner);
			spawner.show();
			player.sendMessage(ChatColor.GREEN + "Spawner added.");
			
			break;
			
		case RIGHT_CLICK_BLOCK:
			if (spawner == null) {
				player.sendMessage(ChatColor.RED + "That is not a spawner.");
				return;
			}
			
			spawner.hide();
			game.removeSpawner(spawner);
			player.sendMessage(ChatColor.GREEN + "Spawner removed.");
			
			break;
			
		default:
			break;
		}
	}

}
