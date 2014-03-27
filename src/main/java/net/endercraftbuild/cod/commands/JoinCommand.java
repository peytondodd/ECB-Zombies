package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;

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
		//if(game.getType() == "")
		try {
			game = plugin.getGameManager().get(args[0]);
			game.addPlayer(player);
			game.broadcast(ChatColor.AQUA + player.getName() + " just joined " + game.getName() + ".");
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
}
