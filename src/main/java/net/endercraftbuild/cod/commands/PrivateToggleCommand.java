package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateToggleCommand implements CommandExecutor {

	private CoDMain plugin;

	public PrivateToggleCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /cprivate (game)
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.toggle")) 
			return true;
		if (args.length < 1)
			return false;

		
		Player player = (Player) sender;
		
		try {
			player.sendMessage(ChatColor.GREEN + "That game has been " + (plugin.getGameManager().get(args[0]).togglePrivate() ? "toggled private" : "toggled public") + ".");
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		return true;
	}
	
}
