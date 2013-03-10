package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WallWeaponSignListener implements Listener {

	private CoDMain plugin;

	public WallWeaponSignListener(CoDMain plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onGunPurchase(PlayerSignEvent event) {
		if (!event.isWeaponSign())
			return;

		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

		if (game == null) {
			player.sendMessage(ChatColor.RED + "Could not find your game!");
			return;
		}

		String name = event.getLine(1);
		Double cost = event.getDouble(2);
		int id = event.getInteger(3);
		
		if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
			player.sendMessage(ChatColor.RED + "You do not have enough money!");
			return;
		}
		
		PlayerInventory inv = player.getInventory();
		ItemStack weapon = new ItemStack(id, 1);
		weapon = Utils.setItemName(weapon, name);
		inv.addItem(weapon);
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onAmmoPurchase(PlayerSignEvent event) {
		if (!event.isAmmoSign())
			return;

		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

		if (game == null) {
			player.sendMessage(ChatColor.RED + "Could not find your game!");
			return;
		}

		Double cost = event.getDouble(1);
		if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
			player.sendMessage(ChatColor.RED + "You do not have enough money!");
			return;
		}
		
		PlayerInventory inv = player.getInventory();
		ItemStack bullet = new ItemStack(337, 64);
		bullet = Utils.setItemName(bullet, "Ammo");
		inv.addItem(bullet);
		player.updateInventory();
	}
	
}
