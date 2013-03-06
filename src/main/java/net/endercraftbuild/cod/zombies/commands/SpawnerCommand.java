package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.admin.SpawnerListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerCommand implements CommandExecutor {

	private CoDMain plugin;

	public SpawnerCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zspawn <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.registerListener(new SpawnerListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Spawn edit mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click a block to add a spawner.");
		player.sendMessage(ChatColor.GREEN + "Right click a spawner to remove it.");
		player.sendMessage(ChatColor.GRAY + "Left click anywhere with a stick to exit spawn edit mode.");

		return true;
	}
	
}