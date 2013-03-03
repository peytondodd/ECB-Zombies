package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.games.Game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerJoinLeaveSignInteractListener implements Listener {
	
	private final CoDMain plugin;
	
	public PlayerJoinLeaveSignInteractListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// TOOD(mortu): this will have to use the game's sign finder once it's done
	@EventHandler(ignoreCancelled = true)
	public void onJoinSignInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (event.getClickedBlock().getType() != Material.SIGN_POST &&
				event.getClickedBlock().getType() != Material.WALL_SIGN)
			return;
		
		Player player = event.getPlayer();
		Game game = plugin.getGameManager().get(player);
		
		if (game != null) {
			player.sendMessage(ChatColor.RED + "You are already in a game!");
			return;
		}
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB CoD"))
			return;
		
		if (!sign.getLine(1).equalsIgnoreCase("Join"))
			return;
		
		if (!player.hasPermission("cod.player.join")) {
			player.sendMessage(ChatColor.RED + "You do not have permission to join this game!");
			return;
		}
		
		game = plugin.getGameManager().get(sign.getLine(2));
		game.addPlayer(player);
		player.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " just joined " + game.getName() + ".");
	}

}
