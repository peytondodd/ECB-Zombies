package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener {

	@SuppressWarnings("unused")
	private CoDMain plugin;

	public BlockListener(CoDMain plugin) {
		this.plugin = plugin;
	}


	@EventHandler
	public void inGameBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!(event.getBlock().getType() == Material.SIGN_POST)) {
			if (!player.isOp()) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)//Prevent land damage
	public void preventLandDestructionOnExplode(EntityExplodeEvent event) {
		event.blockList().clear();
	}
}
