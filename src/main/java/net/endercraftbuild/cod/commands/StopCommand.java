package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopCommand implements CommandExecutor{

	private CoDMain plugin;

	public StopCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.stop")) 
			return true;
		if (args.length < 1) {
			sender.sendMessage(ChatColor.GREEN + "Available games: " + ChatColor.WHITE + plugin.getGameManager().getGameNames().toString());
			return false;
		}
		
		Game game = plugin.getGameManager().get(args[0]);
		
		if (game == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a game by that name!");
			return true;
		} else if (!game.isActive()) {
			sender.sendMessage(ChatColor.RED + "That game is not active!");
			return true;
		}

		try {
			game.stop();
			sender.sendMessage(ChatColor.GREEN + args[0] + " has stopped!");
		} catch (RuntimeException e) {
			sender.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
}
