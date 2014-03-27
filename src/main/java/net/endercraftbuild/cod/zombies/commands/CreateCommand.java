package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements CommandExecutor {

	private CoDMain plugin;

	public CreateCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zcreate <name> <min players> <max players> <zombie multiplier> <max waves>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.zcreate")) 
			return true;
		if (args.length < 5)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = new ZombieGame(plugin);
			game.setSpawnLocation(player.getLocation());
			
			game.setName(args[0]);
			game.setMinimumPlayers(Long.parseLong(args[1]));
			game.setMaximumPlayers(Long.parseLong(args[2]));
			game.setZombieMultiplier(Double.parseDouble(args[3]));
			game.setMaxWaves(Long.parseLong(args[4]));
			
			plugin.getGameManager().add(game);
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Game created with spawn point at your current location.");
		return true;
	}
	
}