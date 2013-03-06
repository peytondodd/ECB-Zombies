package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.admin.DoorListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoorCommand implements CommandExecutor {

	private CoDMain plugin;

	public DoorCommand(CoDMain plugin) {
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
			game.registerListener(new DoorListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Door edit mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click the LOWER block of the door to add it as a door.");
		player.sendMessage(ChatColor.GREEN + "Right click the LOWER block of the door to remove it.");
		player.sendMessage(ChatColor.GRAY + "Left click anywhere with a stick to exit door edit mode.");

		return true;
	}
	
}