package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TextureCommand implements CommandExecutor{

	private CoDMain plugin;

	public TextureCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	// /texture
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		sender.sendMessage(plugin.prefix + "Link: http://bit.ly/ZombieTexture " + ChatColor.RED + "[CLICK IT]");
		return true;

	}
}
