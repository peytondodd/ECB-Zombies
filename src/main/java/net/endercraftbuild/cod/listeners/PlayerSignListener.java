package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerSignListener implements Listener {
	
	private final CoDMain plugin;
	
	public PlayerSignListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		Block block = event.getClickedBlock();
		Material type = block.getType();
		
		if (type != Material.WALL_SIGN && type != Material.SIGN_POST)
			return;
		
		Sign sign = (Sign) block.getState();
		
		if (!ChatColor.stripColor(sign.getLine(0)).toLowerCase().startsWith("ecb"))
			return;
		
		Player player = event.getPlayer();
		
		if (!player.hasPermission("cod.player.signs")) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use that sign!");
			return;
		}
		
		plugin.getServer().getPluginManager().callEvent(new PlayerSignEvent(player, sign));
	}
	
}
