package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdvanceCommand implements CommandExecutor {

	private CoDMain plugin;

	public AdvanceCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zadvance <name>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.zadvance")) 		
			return true;
		if (args.length < 1)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			game.despawnEntities();
			game.advanceWave();
			player.sendMessage(ChatColor.GREEN + game.getName() + " forced to advance to round " + game.getCurrentWave() + ".");
			
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		return true;
	}
	
}