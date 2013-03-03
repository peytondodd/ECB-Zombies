package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand implements CommandExecutor{

	private CoDMain plugin;

	public EditCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zedit <name> <new name> <min players> <max players> <zombie multiplier> <max waves>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 5)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			
			game.setSpawnLocation(player.getLocation());
			
			game.setName(args[1]);
			game.setMinimumPlayers(Long.parseLong(args[2]));
			game.setMaximumPlayers(Long.parseLong(args[3]));
			game.setZombieMultiplier(Double.parseDouble(args[4]));
			game.setMaxWaves(Long.parseLong(args[5]));
			
			plugin.getGameManager().replace(game);
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + "Game updated with spawn point at your current location.");
		return true;
	}
	
}