package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.SpawnerAdminListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerAdminCommand implements CommandExecutor{

	private CoDMain plugin;

	public SpawnerAdminCommand(CoDMain plugin) {
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
			game.showSpawners();
			game.registerListener(new SpawnerAdminListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GREEN + "Spawn edit mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click a block to add a spawner.");
		player.sendMessage(ChatColor.GREEN + "Right click a spawner to remove it.");
		player.sendMessage(ChatColor.GREEN + "Right click the air to exit spawn edit mode.");

		return true;
	}
	
}