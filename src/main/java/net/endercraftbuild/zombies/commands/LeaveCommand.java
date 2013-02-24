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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
		{
			return true;
		}
		Player player = (Player) sender;

		if(Utils.isInGameZ(player) == false)
		{
			player.sendMessage(plugin.prefix + "You are not in a game");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("leave"))
		{
			if (sender.hasPermission("zombies.user"))
			{
				Utils.setInGameZ(player, false);
				//teleport player to spawn
				sender.sendMessage(plugin.prefix + ChatColor.GREEN + "You left {GAMENAME}");
			}
		}
	return true;
	}
}
