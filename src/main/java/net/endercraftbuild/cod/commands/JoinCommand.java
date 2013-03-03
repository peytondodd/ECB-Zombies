package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor{

	private CoDMain plugin;

	public JoinCommand(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			Game game = plugin.getGameManager().get(args[0]);
			game.addPlayer(player);
			player.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " just joined " + game.getName() + ".");
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
		}
		
		return true;
	}
	
}
