package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Door;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DoorSignListener implements Listener {
	
	private final CoDMain plugin;
	
	public DoorSignListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDoorPurchase(PlayerSignEvent event) {
		if (!event.isDoorSign())
			return;
		
		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

		if (game == null) {
			player.sendMessage(ChatColor.RED + "Could not find your game!");
			return;
		}
		
		Sign sign = event.getSign();
		Double cost = event.getDouble(1);
		
		if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
			player.sendMessage(ChatColor.RED + "You do not have enough to open that door!");
			return;
		}
		
		Location location = sign.getLocation();
		for (Door door : game.getDoors())
			if (door.getLocation().distance(location) < 6)
				door.open();
	}
	
}
