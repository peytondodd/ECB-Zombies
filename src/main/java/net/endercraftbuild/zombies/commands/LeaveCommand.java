package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public LeaveCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (cmd.getName().equalsIgnoreCase("leave"))
		{
		if (!(sender instanceof Player))
		{
			return true;
		}
		Player player = (Player) sender;

		if(Utils.isInGameZ(player) == false)
		{
			player.sendMessage(plugin.prefix + "You are not in a zombies game");
			return true;
		}
		if(Utils.isInGamePvP(player) == false)
		{
			player.sendMessage(plugin.prefix + "You are not in a pvp game");
			return true;
		}

			if (sender.hasPermission("zombies.user"))
			{
				if(Utils.isInGameZ(player) == true)
					Utils.setInGameZ(player, false);
				//teleport player to spawn
				sender.sendMessage(plugin.prefix + ChatColor.GREEN + "You left {GAMENAME}");
				player.setExp(0);
			}
			else if(Utils.isInGamePvP(player) == true)
			{
				Utils.setInGamePvP(player, false, Utils.Out);
				sender.sendMessage(plugin.prefix + ChatColor.GREEN + "You left {GAMENAME}");
			}
		}
		{
			return true;
		}
	}
}
