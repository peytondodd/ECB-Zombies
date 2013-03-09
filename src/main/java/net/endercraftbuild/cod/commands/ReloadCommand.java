package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor{

	private CoDMain plugin;

	public ReloadCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.reload();
		sender.sendMessage(ChatColor.GREEN + "ECB CoD configuration has been reloaded!");
		return true;
	}
	
}
