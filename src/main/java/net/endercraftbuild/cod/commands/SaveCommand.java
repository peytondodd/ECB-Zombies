package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand implements CommandExecutor{

	private CoDMain plugin;

	public SaveCommand(CoDMain plugin) {
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