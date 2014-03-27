package net.endercraftbuild.cod.pvp.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.pvp.CoDGame;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PVPCreateCommand implements CommandExecutor {

	private CoDMain plugin;

	public PVPCreateCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /pvpcreate <name> <min players> <max players> 
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!(sender instanceof Player))
				return true;
			if (args.length < 3)
				return false;
			
			Player player = (Player) sender;
			
			try {
				CoDGame game = new CoDGame(plugin);
				game.setSpawnLocation(player.getLocation());
				
				game.setName(args[0]);
				game.setMinimumPlayers(Long.parseLong(args[1]));
				game.setMaximumPlayers(Long.parseLong(args[2]));
		
				
				plugin.getGameManager().add(game);
			} catch (IllegalArgumentException e) {
				player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
				return true;
			}
			
			player.sendMessage(ChatColor.GREEN + "PVP Game created with spawn point at your current location.");
			return true;
		}
		
	}