package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerSignEvent;
import net.endercraftbuild.cod.player.CoDPlayer;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

        if(name.equals("Grenade") && !player.hasPermission("ecb.donor")) {
            player.sendMessage(plugin.prefix + ChatColor.RED + "Grenades are donor only! Sorry! Shop @ ecb-mc.net/shop");
            return;
        }
		
		PlayerInventory inv = player.getInventory();
		ItemStack weapon = new ItemStack(id, 1);
		weapon = Utils.setItemName(weapon, name);
		inv.addItem(weapon);
		player.updateInventory();
		player.sendMessage(plugin.prefix + "Item purchased!");
		player.playSound(player.getLocation(), Sound.ANVIL_USE, 160.0F, 0.0F);
        CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
        cp.giveXp(5);
        cp.setWeaponsBought(cp.getWeaponsBought() + 1);
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
		player.sendMessage(plugin.prefix + ChatColor.RED + "Ammo Purchased!");
		player.playSound(player.getLocation(), Sound.ANVIL_USE, 160.0F, 0.0F);
        CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
        cp.giveXp(1);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPAPPurchase(PlayerSignEvent event) {
		if (!event.isPaPSign())
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
		
		//PlayerInventory inv = player.getInventory();
		ItemStack hand = player.getItemInHand();
		

		if(hand.getType() == Material.GOLD_HOE || hand.getType() == Material.STONE_HOE || hand.getType() == Material.WOOD_HOE || hand.getType() == Material.IRON_HOE ||
				hand.getType() == Material.DIAMOND_HOE || hand.getType() == Material.STONE_PICKAXE || hand.getType() == Material.IRON_PICKAXE || hand.getType() == Material.GOLD_PICKAXE) {
			
			if(Utils.isGunPaP(hand)) {
				player.sendMessage(plugin.prefix + "That is already Pack-a-Punched!");
				return;
			}
		
		Utils.setItemName(hand, Utils.getItemName(hand) + " PaP");
		player.getInventory().setItemInHand(Utils.addGlow(player.getItemInHand()));
		player.updateInventory();
		player.sendMessage(plugin.prefix + ChatColor.RED + "Pack-A-Punched!");
		player.playSound(player.getLocation(), Sound.ANVIL_LAND, 160.0F, 0.0F);
        CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
        cp.giveXp(10);

		}
		
	}
}
	

