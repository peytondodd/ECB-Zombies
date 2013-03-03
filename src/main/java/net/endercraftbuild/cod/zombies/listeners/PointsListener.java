package net.endercraftbuild.cod.zombies.listeners;

import java.util.Random;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PointsListener implements Listener {

	private CoDMain plugin;

	public PointsListener(CoDMain plugin) {
		this.plugin = plugin;
	}

	//TODO: Add an error free listener for gun kills

	@EventHandler
	public void ZombieDeath(EntityDeathEvent event) {

		Player player = event.getEntity().getKiller();
		if(player != null && player instanceof Player)
		{
			Player killerPlayer = (Player)player;
			if(Utils.isInGameZ(killerPlayer))
			{
			if (event.getEntity().getType().equals(EntityType.ZOMBIE) || event.getEntity().getType().equals(EntityType.WOLF)) {
				Random random = new Random();
				int amount = (new Double(1.0 + random.nextDouble() * 15.0)).intValue();
				EconomyResponse r = plugin.getEconomy().depositPlayer(killerPlayer.getName(), amount);
				if(r.transactionSuccess()) {
					killerPlayer.sendMessage(plugin.prefix + ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + plugin.getEconomy().getBalance(killerPlayer.getName())
							+ ChatColor.GREEN + " points! Gained: " + ChatColor.DARK_GREEN + amount);
				}
			}
		}
	}
}
}
