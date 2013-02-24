package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public CreateCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
		{
			return true;
		}
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("create")) {		
			//Do stuff
			// create (gamename) (max players (zombie multipliers)
			Location loc = player.getLocation();
			//Sets game spawn in location player is standing in when running this command
		}
		return true;
	}
}