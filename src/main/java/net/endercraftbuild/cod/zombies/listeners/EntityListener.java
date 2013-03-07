package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityListener implements Listener {
	
	@SuppressWarnings("unused")
	private CoDMain plugin;

	public EntityListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// TODO(mortu): move to mechanics listener
	@EventHandler
	public void BurningWolf(EntityDamageEvent event) {
		if (event.getEntity() instanceof Wolf) {
			if (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
				event.setCancelled(true);
			}
			//No explosion damage to players in ZOMBIE games only!
			else if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();

				if (Utils.isInGameZ(player) == true) {
					if (event.getCause().equals(DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	// TODO(mortu): move to mechanics listener
	@EventHandler(ignoreCancelled = true)
	public void hellhound(CreatureSpawnEvent event) {
		Entity ent = event.getEntity();
		
		if (ent instanceof Wolf) {
			Wolf w = (Wolf) ent;
			w.setFireTicks(1000000000); //Keep him on fire for a long time...
		}
	}
}




