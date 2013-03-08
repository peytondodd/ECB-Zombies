package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

	private CoDMain plugin;

	public PlayerListener(CoDMain plugin) {
		this.plugin = plugin;
	}

	// TODO(mortu): clean up implementation
	@EventHandler
	public void MysteryBox(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block.getType() == Material.CHEST)
		{
			Sign sign = (Sign)block.getRelative(BlockFace.UP, 1).getState();
			if(sign.getType() == Material.WALL_SIGN)
			{
				if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB Zombies"))
				{
					double amount = 0;
					try {
						amount = Double.parseDouble(sign.getLine(1));
					} catch (NumberFormatException ex) {
						player.sendMessage(ChatColor.RED + ex.toString());
						EconomyResponse r = plugin.getEconomy().withdrawPlayer(player.getName(), amount);
						if(r.transactionSuccess())
						{
						}
						//Open the chest with 1 of the random items
						else
							player.sendMessage(plugin.prefix + ChatColor.RED + "You need " + amount + " to use the Mystery Box!");
						event.setCancelled(true);
					}
				}
			}
			{
			}

		}
	}
}

