package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;

import org.bukkit.Bukkit;
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
			if(args.length == 1) 
			{
				String gamename = "read gamename from file";
				if(args[0].equalsIgnoreCase(gamename))
				{
				if (sender.hasPermission("zombies.user"))
				{
				Utils.setInGame(player, false);
				//teleport player to game
				Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + player.getName() + " just joined" + gamename);
					}
				}
			}
		}
		return true;	
	}
}
