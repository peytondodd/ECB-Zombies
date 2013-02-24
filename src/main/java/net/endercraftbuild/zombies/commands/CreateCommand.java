package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.games.Game;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public CreateCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}
	
	// /create <name> <min players> <max players> <zombie multiplier> <max waves>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 5)
			return false;
		
		Player player = (Player) sender;
		
		try {
			Game game = new Game();
			game.setSpawnLocation(player.getLocation());
			
			game.setName(args[0]);
			game.setMininumPlayers(Long.parseLong(args[1]));
			game.setMaximumPlayers(Long.parseLong(args[2]));
			game.setZombieMultiplier(Double.parseDouble(args[3]));
			game.setMaxWaves(Long.parseLong(args[4]));
			
			plugin.getGameManager().put(game.getName(), game);
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Game created with spawn point at your current location.");
		return true;
	}
	
}