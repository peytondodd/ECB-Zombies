package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.admin.AdminSignListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbySignCommand implements CommandExecutor {
	
	private CoDMain plugin;

	public LobbySignCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /lobbysign <game>
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("game.admin.lobby")) 
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.registerListener(new AdminSignListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Sign edit mode activated.");
		player.sendMessage(ChatColor.GRAY + "Left click anywhere with a stick to exit sign edit mode.");

		return true;
	}
	
}
