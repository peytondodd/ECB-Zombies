package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.listeners.admin.LinkListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {

	private CoDMain plugin;

	public LinkCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zlink <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.registerListener(new LinkListener(game, player));
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		player.sendMessage(ChatColor.GOLD + "Link mode activated.");
		player.sendMessage(ChatColor.GREEN + "Left click the LOWER block of the door to link it. Link all door blocks from wide doors.");
		player.sendMessage(ChatColor.GREEN + "Left click all spawners to link to selected doors.");
		player.sendMessage(ChatColor.GRAY + "Left click anywhere with a stick to exit link mode.");
		player.sendMessage(ChatColor.AQUA + "Hiding barriers to make it easer to walk around.");

		return true;
	}
	
}