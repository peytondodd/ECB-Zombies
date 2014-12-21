package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;

import net.endercraftbuild.cod.player.CoDPlayer;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor{

	private CoDMain plugin;

	public JoinCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1) {
			sender.sendMessage(ChatColor.GREEN + "Available games: " + ChatColor.WHITE + plugin.getGameManager().getGameNames().toString());
			return false;
		
		}
		
		Player player = (Player) sender;
		Game game = plugin.getGameManager().get(player);
		
		if (game != null) {
			player.sendMessage(ChatColor.RED + "You are already in a game!");
			return true;
		}

		try {
			game = plugin.getGameManager().get(args[0]);

            CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
            if(cp.getLevel() < game.getMinLevel()) {
                player.sendMessage(plugin.prefix + ChatColor.BOLD + "You are NOT a high enough level to join this game! Required: " + game.getMinLevel());
                return true;
            }

			game.addPlayer(player);
			game.broadcast(ChatColor.AQUA + player.getName() + " just joined " + game.getName() + ".");
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
}
