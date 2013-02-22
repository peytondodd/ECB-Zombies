package main.java.net.endercraftbuild.zombies.listeners;

import main.java.net.endercraftbuild.zombies.ZombiesMain;
import main.java.net.endercraftbuild.zombies.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener implements Listener {

	private ZombiesMain plugin;

	public PlayerListener(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler//Join Signs
	public void Join(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if(Utils.isInGame(player) == true)
			player.sendMessage(plugin.prefix + ChatColor.RED + "You are already in the game!");
		else
			
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN)) 
			{
				Sign sign = (Sign)event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase("§9ECB Zombies")) 
				{
					if (sign.getLine(1).equalsIgnoreCase("Join") && player.hasPermission("zombies.user")) 
					{
						//Location loc = read coords from file to tp  player to game
						//Set player in some game array or something
						String game = "get game info from fileeeeee";
						player.sendMessage(plugin.prefix + ChatColor.GREEN + "You have joined " + game);
					}
					else {
						player.sendMessage(plugin.prefix + ChatColor.RED + "Failed to join game!");
					}
				}
			}
		}
	}
	@EventHandler//Create join signs
	public void SignCreate(SignChangeEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if (player.hasPermission("zombies.signs.create")) 
		{
			if (event.getLine(0).equalsIgnoreCase("ECB Zombies")) 
			{		
				event.setLine(0, "§9ECB Zombies");
				player.sendMessage(plugin.prefix + ChatColor.GREEN + " Join sign created!");
			}
		}
	}

	@EventHandler
	public void noPerm(SignChangeEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if (!player.hasPermission("zombies.signs.create")) 
		{
			if (event.getLine(0).equalsIgnoreCase("ECB Zombies")) 
			{
				event.setLine(0, "§4No Permission!");
				player.sendMessage(plugin.prefix + ChatColor.RED + " No permissions!");
			}
		}
	}

@EventHandler//Walll gunzzzz
public void WallWeapons(PlayerInteractEvent event) {
	if (event.isCancelled()) return;
	Player player = event.getPlayer();
	if(Utils.isInGame(player) == true)
	{
	if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
	{
		if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN)) 
		{
			Sign sign = (Sign)event.getClickedBlock().getState();
			if (sign.getLine(0).equalsIgnoreCase("§9ECB Zombies")) 
			{
				if (player.hasPermission("zombies.user")) 
				{
					//Line 1 can be the name EX: "Grenades"
					double amount = 0;
					try {
						amount = Double.parseDouble(sign.getLine(3));
					} catch (NumberFormatException ex) {
						player.sendMessage(ChatColor.RED + ex.toString());

					}
					EconomyResponse r = ZombiesMain.economy.withdrawPlayer(player.getName(), amount);
					if(r.transactionSuccess())
					{
					PlayerInventory inv = player.getInventory();
					int id = Integer.parseInt(sign.getLine(2));
					ItemStack weapon = new ItemStack(id, 1);
					inv.addItem(weapon);
					player.sendMessage(plugin.prefix + ChatColor.GREEN + "You have purchased " + sign.getLine(1));
					}
					else {
						player.sendMessage(plugin.prefix + ChatColor.GREEN + "You need " + ChatColor.DARK_GREEN + amount 
								+ ChatColor.GREEN + " to buy " + ChatColor.DARK_GREEN + sign.getLine(1));
					}
				}
			}
		}
	}
}
}
}