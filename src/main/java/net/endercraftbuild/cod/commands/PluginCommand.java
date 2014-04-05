package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PluginCommand implements CommandExecutor{

	private CoDMain plugin;

	public PluginCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	//  /pl, /?
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		sender.sendMessage(plugin.prefix + ChatColor.RED + "90% of our plugins are custom made and loaded with sweg.");
		return true;

	}
}
