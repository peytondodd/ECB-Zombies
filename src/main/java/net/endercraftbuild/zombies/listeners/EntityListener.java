package net.endercraftbuild.zombies.listeners;

import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.utils.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
			//No explosion damage to players in ZOMBIE games only!
			else if(event.getEntity() instanceof Player)
			{ 
				Player player = (Player) event.getEntity();

				if(Utils.isInGameZ(player) == true)
				{
					if(event.getCause().equals(DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(DamageCause.BLOCK_EXPLOSION))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void hellhound(CreatureSpawnEvent event) {
		if(event.isCancelled())
			return;

		Entity ent = event.getEntity();
		if(ent instanceof Wolf) {
			Wolf w = (Wolf)event.getEntity();
			w.setFireTicks(1000000000); //Keep him on fire for a long time...
		}
	}
}




