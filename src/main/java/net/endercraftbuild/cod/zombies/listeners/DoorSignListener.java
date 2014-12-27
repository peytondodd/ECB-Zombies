package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;
import net.endercraftbuild.cod.player.CoDPlayer;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Door;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
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
		
		if (!plugin.getEconomy().has(player.getName(), cost)) {
			player.sendMessage(ChatColor.RED + "You do not have enough to open that door!");
			return;
		}
	
		
		boolean foundDoor = false;
		Location location = sign.getLocation();
		for (Door door : game.getDoors())
			if (door.getLocation().distance(location) < 6) {
				if (door.isOpen()) { //why...?
                    player.sendMessage(plugin.prefix + "Door already open!");
                    return;
                }

				
				door.open();
				foundDoor = true;
                plugin.getEconomy().withdrawPlayer(player.getName(), cost);
                player.sendMessage(plugin.prefix + ChatColor.RED + "Door opened!");
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 160.0F, 0.0F);
                CoDPlayer c = plugin.getPlayerManager().getPlayer(player);
                c.incrementDoors();
                c.giveXp(8);

			}


	}
	
}
