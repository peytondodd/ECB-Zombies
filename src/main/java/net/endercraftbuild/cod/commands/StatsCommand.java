package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.pvp.CoDGame;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor{

	private CoDMain plugin;

	public StatsCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		try {
			Game game = args.length == 0 ? plugin.getGameManager().get(player) : plugin.getGameManager().get(args[0]);
			
			if (game == null) {
				player.sendMessage(ChatColor.RED + "You are not currently in a game, please specify one.");
				player.sendMessage(ChatColor.GREEN + "Available games: " + ChatColor.WHITE + plugin.getGameManager().getGameNames().toString());
				return true;
			} else if (!game.isActive()) {
				player.sendMessage(ChatColor.RED + "That game is not runnning at the moment.");
				return true;
			}
				
			if (game instanceof ZombieGame)
				doZombieStats(player, (ZombieGame) game);
			else if (game instanceof CoDGame)
				doCoDStats(player, (CoDGame) game);
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
	private void doZombieStats(Player player, ZombieGame game) {
		player.sendMessage(Utils.formatMessage("&7Game: &c%s", game.getName()));
		player.sendMessage(Utils.formatMessage("&7Wave: &c%d&7 of &c%d&7 (max)", game.getCurrentWave(), game.getMaxWaves()));
		player.sendMessage(Utils.formatMessage("&7Players: &c%d&7 of &c%d&7 (max)", game.getPlayers().size(), game.getMaximumPlayers()));
		player.sendMessage(Utils.formatMessage("&7Enemies alive: &c%d&7 of &c%d&7 (this round)", game.getLivingEntityCount(), game.getMaxEntityCount()));
	}
	
	private void doCoDStats(Player player, CoDGame game) {
		// TODO(mortu): implement CoDGame stats
	}
	
}
