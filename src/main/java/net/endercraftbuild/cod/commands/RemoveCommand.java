package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand implements CommandExecutor {

	private CoDMain plugin;

	public RemoveCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /cremove <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.remove")) 
			return true;
		if (args.length < 1)
			return false;
		
	
	
			Player player = (Player) sender;
		
		try {
			plugin.getGameManager().remove(plugin.getGameManager().get(args[0]));
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "That game has been removed.");
		return true;
	}
	
}
	