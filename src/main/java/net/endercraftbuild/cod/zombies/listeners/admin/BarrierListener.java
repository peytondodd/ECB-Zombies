package net.endercraftbuild.cod.zombies.listeners.admin;

import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Barrier;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BarrierListener implements Listener {
	
	private final ZombieGame game;
	private final Player player;

	public BarrierListener(ZombieGame game, Player player) {
		this.game = game;
		this.player = player;
		game.showBarriers();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		Block block = event.getClickedBlock();
		Barrier barrier = game.findBarrier(block.getLocation());
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (barrier != null) {
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
			if (barrier == null) {
				player.sendMessage(ChatColor.RED + "That is not a barrier.");
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
