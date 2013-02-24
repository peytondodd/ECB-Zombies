package net.endercraftbuild.pvp.listeners;

import net.endercraftbuild.zombies.ZombiesMain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
