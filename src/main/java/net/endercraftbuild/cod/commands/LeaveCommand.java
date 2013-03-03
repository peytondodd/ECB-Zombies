package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor{

	private CoDMain plugin;

	public LeaveCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		try {
			Game game = plugin.getGameManager().get(player);
			if (game == null)
				throw new RuntimeException("You are not in a game.");
			game.removePlayer(player);
			player.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " just left " + game.getName() + ".");
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
}
