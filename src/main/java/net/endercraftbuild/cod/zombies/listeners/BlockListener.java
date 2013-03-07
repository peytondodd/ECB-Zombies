package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener {

	@SuppressWarnings("unused")
	private CoDMain plugin;

	public BlockListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// TODO(mortu): move to mechanics listener
	@EventHandler (priority = EventPriority.LOW) //Prevent land damage
	public void preventLandDestructionOnExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
	
}
