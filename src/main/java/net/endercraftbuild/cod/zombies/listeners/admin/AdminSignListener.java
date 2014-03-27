package net.endercraftbuild.cod.zombies.listeners.admin;



import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.LobbySign;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdminSignListener extends AdminListener {
	public static CoDMain plugin;
	public AdminSignListener(ZombieGame game, Player player) {
		super(game, player);
	
	}
	


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() != player)
			return;
		
		if (isExitEvent(event)) {
	
			game.unregisterListener(this);
			player.sendMessage(ChatColor.GOLD + "Back to normal mode.");
			return;
		}
		
		Block block = event.getClickedBlock();
		LobbySign sign  = block != null ? game.findSign(block.getLocation()) : null;
		
		switch (event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if (sign != null) {
				player.sendMessage(ChatColor.RED + "That is already a loot.");
				return;
			}
			
			sign = new LobbySign(plugin);
			sign.setLocation(block.getLocation());
			game.addLobbySign(sign);
			
			player.sendMessage(ChatColor.GREEN + "Sign added.");
			
			break;
			
		case RIGHT_CLICK_BLOCK:
			if (sign == null) {
				player.sendMessage(ChatColor.RED + "That is not a sign.");
				return;
			}
			
			game.removeLobbySign(sign);
			player.sendMessage(ChatColor.GREEN + "sign removed.");
			
			break;
			
		default:
			break;
		}
	}

}

