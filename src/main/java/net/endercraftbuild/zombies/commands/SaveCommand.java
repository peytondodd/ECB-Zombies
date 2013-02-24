package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public SaveCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}
	
	// /save
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		plugin.getGameManager().save();
		sender.sendMessage(ChatColor.GREEN + "Current game setups saved.");
		return true;
	}
	
}