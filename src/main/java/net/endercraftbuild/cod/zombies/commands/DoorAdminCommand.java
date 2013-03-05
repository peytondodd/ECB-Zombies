package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.DoorAdminListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoorAdminCommand implements CommandExecutor{

	private CoDMain plugin;

	public DoorAdminCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zdoor <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.showDoors();
			game.registerListener(new DoorAdminListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GREEN + "Door edit mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click the LOWER block of the door to add it as a door.");
		player.sendMessage(ChatColor.GREEN + "Right click the LOWER block of the door to remove it.");
		player.sendMessage(ChatColor.GREEN + "Right click the air to exit door edit mode.");

		return true;
	}
	
}