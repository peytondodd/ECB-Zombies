package net.endercraftbuild.zombies.listeners;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Snowball;

public class PlayerListener implements Listener {

	private ZombiesMain plugin;

	public PlayerListener(ZombiesMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true) //Join Signs
	public void udiedbruh(PlayerDeathEvent event)
	{
		event.setDroppedExp(0);
		//save inv if revived
	}
	public void leave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		Utils.setInGamePvP(player, false, Utils.Out);
		Utils.setInGameZ(player, false);
	}


	@EventHandler
	public void MysteryBox(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(Utils.isInGameZ(player))
		{
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
							EconomyResponse r = ZombiesMain.economy.withdrawPlayer(player.getName(), amount);
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
}
