package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MobCommand implements CommandExecutor {

	private CoDMain plugin;

	public MobCommand(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /zmob <name> <wave> <type>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if (args.length < 2)
			return false;
		
		Player player = (Player) sender;
		
		try {
			ZombieGame game = (ZombieGame) plugin.getGameManager().get(args[0]);
			
			Integer wave = Integer.parseInt(args[1]);
			String type = args.length == 3 ? args[2] : null;
			
			game.setWaveMob(wave, type);
			
			if (type != null)
				player.sendMessage(ChatColor.GREEN + game.getName() + " will now spawn " + type + " on waves that are multiples of " + wave + ".");
			else
				player.sendMessage(ChatColor.GREEN + game.getName() + " will now spawn zombies on waves that are multiples of " + wave + ".");
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
			return true;
		}

		return true;
	}
	
}