package net.endercraftbuild.zombies.commands;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.games.Game;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand implements CommandExecutor{

	private ZombiesMain plugin;

	public EditCommand(ZombiesMain plugin) {
		this.plugin = plugin;
	}
	
	// /edit <name> <new name> <min players> <max players> <zombie multiplier> <max waves>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 5)
			return false;
		
		Player player = (Player) sender;
		
		try {
			Game game = plugin.getGameManager().get(args[0]);
			if (game == null)
				throw new IllegalArgumentException("There is no game by that name.");
			
			game.setSpawnLocation(player.getLocation());
			
			game.setName(args[1]);
			game.setMininumPlayers(Long.parseLong(args[2]));
			game.setMaximumPlayers(Long.parseLong(args[3]));
			game.setZombieMultiplier(Double.parseDouble(args[4]));
			game.setMaxWaves(Long.parseLong(args[5]));
			
			plugin.getGameManager().put(game.getName(), game);
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Game updated with spawn point at your current location.");
		return true;
	}
	
}