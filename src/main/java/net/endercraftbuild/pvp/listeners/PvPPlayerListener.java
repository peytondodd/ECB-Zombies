package net.endercraftbuild.pvp.listeners;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PvPPlayerListener implements Listener	{

	private ZombiesMain plugin;

	public PvPPlayerListener(ZombiesMain plugin) {
		this.plugin = plugin;
	}
@EventHandler
public void onPlayerDeath(PlayerDeathEvent event) {
	if ((event.getEntity() instanceof Player))
		this.plugin.kills.remove(event.getEntity().getName());
	if ((event.getEntity().getKiller() instanceof Player)) {
		Player killer = event.getEntity().getKiller();
		if(Utils.isInGamePvP(killer)) 
		{	
		Integer totalkill = (Integer)this.plugin.kills.get(killer.getName());
		if (totalkill == null)
			this.plugin.kills.put(killer.getName(), Integer.valueOf(1));
		else {
			this.plugin.kills.put(killer.getName(), Integer.valueOf(totalkill.intValue() + 1));
		}
		if ((totalkill.intValue() == 5) && (totalkill != null))
			this.plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.RED + killer.getName() + ChatColor.GOLD + " has a kill streak of 5!");
		else if ((totalkill.intValue() == 10) && (totalkill != null))
			this.plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.RED + killer.getName() + ChatColor.GOLD + " has a kill streak of 10!");
		}
	}
}


@EventHandler(ignoreCancelled = true) //Join for pvpSigns
public void Join(PlayerInteractEvent event) 
{
	Player player = event.getPlayer();
	if(Utils.isInGamePvP(player) )
		player.sendMessage(plugin.prefix + ChatColor.RED + "You are already in the game!");
	else
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
		if (event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.WALL_SIGN))
		{
		Sign sign = (Sign)event.getClickedBlock().getState();
		if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ECB TDM"))
		{
			if (ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("Join"))
			{
			//if(ChatColor.stripColor(sign.getLine(2)).equalsIgnoreCase(mapname)) {
			
			if(Utils.ISA.size() > Utils.Mercs.size())
				//teleport to game locations
				//Give starter items
				Utils.setInGamePvP(player, true, Utils.Mercs);
				else {
					Utils.setInGamePvP(player, true, Utils.ISA);
					}
					
				}
			}
		}
	}
}
}
					
					
					
					
					
					