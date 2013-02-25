package net.endercraftbuild.zombies.listeners;

import net.endercraftbuild.zombies.ZombiesMain;

import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityListener implements Listener
{
	private ZombiesMain plugin;

	public EntityListener(ZombiesMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void BurningWolf(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Wolf)
		{
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK))
			{
				event.setCancelled(true);
			}
		}
	}
}
