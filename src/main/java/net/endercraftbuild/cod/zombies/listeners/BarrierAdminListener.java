package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.zombies.Barrier;
import net.endercraftbuild.cod.zombies.Spawner;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Wool;

public class BarrierAdminListener implements Listener {
	
	private final ZombieGame game;
	private final Player player;

	public BarrierAdminListener(ZombieGame game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		Block block = event.getClickedBlock();
		Barrier barrier = null;
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (block.getType() == Material.WOOL && ((Wool) block).getColor() == DyeColor.PINK) {
				player.sendMessage(ChatColor.RED + "That is already a barrier.");
				return;
			}
			
			barrier = new Barrier();
			barrier.setLocation(block.getLocation());
			game.addBarrier(barrier);
			barrier.show();
			player.sendMessage(ChatColor.GREEN + "Barrier added.");
			
			break;
			
		case RIGHT_CLICK_BLOCK:
			if (block.getType() != Material.WOOL || ((Wool) block).getColor() != DyeColor.PINK) {
				player.sendMessage(ChatColor.RED + "That is not a barrier.");
				return;
			}
			
			barrier = game.findBarrier(block.getLocation());
			
			if (barrier == null) {
				player.sendMessage(ChatColor.RED + "That is not a barrier, just a piece of pink wool you left laying around.");
				return;
			}
			
			barrier.hide();
			game.removeBarrier(barrier);
			player.sendMessage(ChatColor.GREEN + "Barrier removed.");
			
			break;
			
		case RIGHT_CLICK_AIR:
			game.hideBarriers();
			game.unregisterListener(this);
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			break;
			
		default:
			break;
		}
	}

}
