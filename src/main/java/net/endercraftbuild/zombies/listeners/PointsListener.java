package main.java.net.endercraftbuild.zombies.listeners;

import java.util.Random;

import main.java.net.endercraftbuild.zombies.ZombiesMain;
import main.java.net.endercraftbuild.zombies.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PointsListener implements Listener {

	private ZombiesMain plugin;

	public PointsListener(ZombiesMain plugin) {
		this.plugin = plugin; 
	}
	
	//TODO: Add an error free listener for gun kills
	
	@EventHandler
	public void ZombieDeath(EntityDeathEvent event) {
		
		Player player = event.getEntity().getKiller();
		if(player != null && player instanceof Player)
		{
			Player killerPlayer = (Player)player;
			if(Utils.isInGame(killerPlayer))
			{
			if (event.getEntity().getType().equals(EntityType.ZOMBIE) || event.getEntity().getType().equals(EntityType.WOLF)) {
				Random random = new Random();
				int amount = (new Double(1.0 + random.nextDouble() * 15.0)).intValue();
				EconomyResponse r = ZombiesMain.economy.depositPlayer(killerPlayer.getName(), amount);
				if(r.transactionSuccess()) {
					killerPlayer.sendMessage(plugin.prefix + ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + ZombiesMain.economy.getBalance(killerPlayer.getName())
							+ ChatColor.GREEN + " points! Gained: " + ChatColor.DARK_GREEN + amount);
				}
			}
		}
	}
}
}