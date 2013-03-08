package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SignListener implements Listener {

	private CoDMain plugin;

	public SignListener(CoDMain plugin) {
		this.plugin = plugin;
	}

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

		Sign sign = event.getSign();
		Double cost = event.getDouble(3);
		String name = sign.getLine(1);
		int id = Integer.parseInt(sign.getLine(2));
		EconomyResponse r = plugin.getEconomy().withdrawPlayer(player.getName(), cost);
		if(r.transactionSuccess())
		{
			PlayerInventory inv = player.getInventory();
			ItemStack weapon = new ItemStack(id, 1);
			weapon = Utils.setItemName(weapon, name);
			inv.addItem(weapon);
			if (!plugin.getEconomy().has(player.getName(), cost)) {
				player.sendMessage(ChatColor.RED + "You do not have enough money!");
				return;
			} else if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
				player.sendMessage(ChatColor.RED + "Failed to buy weapon!");
				return;
			}
		}
	}
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
		EconomyResponse r = plugin.getEconomy().withdrawPlayer(player.getName(), cost);
		if(r.transactionSuccess())
		{
			PlayerInventory inv = player.getInventory();
			ItemStack bullet = new ItemStack(337, 64);
			bullet = Utils.setItemName(bullet, "Ammo");
			inv.addItem(bullet);
			if (!plugin.getEconomy().has(player.getName(), cost)) {
				player.sendMessage(ChatColor.RED + "You do not have enough money!");
				return;
			} else if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
				player.sendMessage(ChatColor.RED + "Failed to buy ammo!");
				return;
			}
		}
	}
}
