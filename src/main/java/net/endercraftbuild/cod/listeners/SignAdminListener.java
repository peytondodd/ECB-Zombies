package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignAdminListener implements Listener {
	
	@SuppressWarnings("unused")
	private final CoDMain plugin;
	
	public SignAdminListener(CoDMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (!ChatColor.stripColor(event.getLine(0)).toLowerCase().startsWith("ecb"))
			return;

		Player player = event.getPlayer();

		if (player.hasPermission("cod.admin.signs")) {
			event.setLine(0, ChatColor.BLUE + event.getLine(0));
			player.sendMessage(ChatColor.GREEN + "Sign created!");
			
			// TODO(mortu): register sign if it's a join/status sign
		} else {
			event.setLine(0, ChatColor.DARK_RED + "No Permission!");
			player.sendMessage(ChatColor.RED + "You do not have permission to place that sign!");
		}
	}

}
