package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.events.PlayerSignEvent;

import net.endercraftbuild.cod.player.CoDPlayer;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JoinSignListener implements Listener {
	
	private final CoDMain plugin;
	
	public JoinSignListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoinSignInteract(PlayerSignEvent event) {
		if (!event.isJoinSign())
			return;
		
		Player player = event.getPlayer();

		if (!player.hasPermission("cod.player.join")) {
			player.sendMessage(ChatColor.RED + "You do not have permission to join this game!");
			return;
		}
		
		Sign sign = event.getSign();
		Game game = plugin.getGameManager().get(player);
		
		if (game != null) {
			player.sendMessage(ChatColor.RED + "You are already in a game!");
			return;
		}





		try {


			game = plugin.getGameManager().get(sign.getLine(1));

            CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
            if(cp.getLevel() < game.getMinLevel()) {
                player.sendMessage(plugin.prefix + ChatColor.BOLD + "You are NOT a high enough level to join this game! Required: " + game.getMinLevel());
                return;
            }

			game.addPlayer(player);
			game.broadcast(ChatColor.AQUA + player.getName() + " just joined " + game.getName() + ".");
			//player.getServer().broadcastMessage(ChatColor.AQUA + player.getName() + " just joined " + game.getName() + ".");
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
	}
	
}
