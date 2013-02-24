package main.java.net.endercraftbuild.zombies.commands;

import main.java.net.endercraftbuild.zombies.ZombiesMain;
import main.java.net.endercraftbuild.zombies.utils.Utils;

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
		{}
		Player player = (Player) sender;

		if(Utils.isInGame(player) == false) {}

		if (cmd.getName().equalsIgnoreCase("leave"))
		{
			if (sender.hasPermission("zombies.user"))
			{
				Utils.setInGame(player, false);
				//teleport player to gamae
				sender.sendMessage(plugin.prefix + ChatColor.GREEN + "You leave {GAMENAME}");
			}
		}
		{
	return true;
}
}
}
