package main.java.net.endercraftbuild.zombies.commands;

import main.java.net.endercraftbuild.zombies.ZombiesMain;
import main.java.net.endercraftbuild.zombies.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public JoinCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) 
		{
			return true;
		}
		Player player = (Player) sender;

		if(Utils.isInGame(player) == true)
		{
			player.sendMessage(plugin.prefix + "Please leave the game you are in before joining another");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("join"))
		{
			if (sender.hasPermission("zombies.user"))
			{
				Utils.setInGame(player, false);
				//teleport player to game
				sender.sendMessage(plugin.prefix + ChatColor.GREEN + "You joined {GAMENAME}");
			}
		}
	return true;
	}
}
