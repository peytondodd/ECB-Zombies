package net.endercraftbuild.zombies.listeners;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;
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

public class SignListener implements Listener {

	private ZombiesMain plugin;

	public SignListener(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true) //Join Signs
	public void Join(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(Utils.isInGameZ(player) )
			player.sendMessage(plugin.prefix + ChatColor.RED + "You are already in the game!");
		else

			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
				{
					Sign sign = (Sign)event.getClickedBlock().getState();
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB Zombies"))
					{
						if (sign.getLine(1).equalsIgnoreCase("Join") && player.hasPermission("zombies.user"))
						{
							//Location loc = read coords from file to tp  player to game
							Utils.setInGameZ(player, true);
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
	@EventHandler(ignoreCancelled = true) //Create join signs
	public void SignCreate(SignChangeEvent event) {
		if (!ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("ECB Zombies"))
			return;

		Player player = event.getPlayer();

		if (player.hasPermission("zombies.signs.create")) {
			event.setLine(0, ChatColor.BLUE + "ECB Zombies");
			player.sendMessage(plugin.prefix + ChatColor.GREEN + "Sign created!");
		} else {
			event.setLine(0, ChatColor.DARK_RED + "No Permission!");
			player.sendMessage(plugin.prefix + ChatColor.RED + "No permissions!");
		}
	}

	@EventHandler(ignoreCancelled = true)//Walll gunzzzz
	public void WallWeapons(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(Utils.isInGameZ(player))
		{
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
				{
					Sign sign = (Sign)event.getClickedBlock().getState();
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB Zombies"))
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
								weapon = Utils.setItemName(weapon, sign.getLine(1));
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


	@EventHandler(ignoreCancelled = true)//Walll gunzzzz
	public void pap(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(Utils.isInGameZ(player))
		{
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
				{
					Sign sign = (Sign)event.getClickedBlock().getState();
					if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB Zombies"))
					{
						if (player.hasPermission("zombies.user"))
						{
							if (ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("Pack-a-Punch"))
							{
								double amount = 0;
								try {
									amount = Double.parseDouble(sign.getLine(3));
								} catch (NumberFormatException ex) {
									player.sendMessage(ChatColor.RED + ex.toString());

								}
								EconomyResponse r = ZombiesMain.economy.withdrawPlayer(player.getName(), amount);
								if(r.transactionSuccess())
								{
									//make pack a punched
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
}


