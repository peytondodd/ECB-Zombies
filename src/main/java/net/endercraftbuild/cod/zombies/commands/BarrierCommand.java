package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.admin.BarrierListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BarrierCommand implements CommandExecutor{

	private CoDMain plugin;

	public BarrierCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zbarrier <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.registerListener(new BarrierListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GREEN + "Barrier edit mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click the LOWER block of the barrier to add it as a barrier.");
		player.sendMessage(ChatColor.GREEN + "Right click the LOWER block of the barrier to remove it.");
		player.sendMessage(ChatColor.GREEN + "Right click the air to exit barrier edit mode.");

		return true;
	}
	
}