package net.endercraftbuild.cod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerLowEvent;

public class PlayerHealthListener implements Listener {
	
	private final CoDMain plugin;
	
	public PlayerHealthListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			if(player.getHealth() <= 4) //health of 4 = 2 hearts, 1 = 1/2 of a heart. you can adjust this if you like
			{
				plugin.getServer().getPluginManager().callEvent(new PlayerLowEvent(player, plugin.getGameManager().get(player)));
			}
		}
	}
	
}
