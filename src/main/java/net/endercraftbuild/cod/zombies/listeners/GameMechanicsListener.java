package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameMechanicsListener implements Listener {

	private CoDMain plugin;

	public GameMechanicsListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event) { // don't let zombie players kill each other
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(Utils.getDamager(event) instanceof Player))
			return;
		
		Player player = (Player) event.getEntity();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerHitByExplosion(EntityDamageEvent event) { // don't let zombie game players be injured by explosions
		if (!(event.getEntity() instanceof Player))
			return;
		if (!event.getCause().equals(DamageCause.ENTITY_EXPLOSION) && !event.getCause().equals(DamageCause.BLOCK_EXPLOSION))
			return;
			
		Player player = (Player) event.getEntity();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onWolfBurning(EntityDamageEvent event) { // make sure they're immune to fire
		if (!(event.getEntity() instanceof Wolf))
			return;
		if (!event.getCause().equals(DamageCause.FIRE) && !event.getCause().equals(DamageCause.FIRE_TICK))
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onWolfSpawn(CreatureSpawnEvent event) { // spawn it as a hellhound
		Entity entity = event.getEntity();

		if (!(entity instanceof Wolf))
			return;
		
		Wolf w = (Wolf) entity;
		w.setFireTicks(1000000000);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onItemUsed(PlayerInteractEvent event) { // don't allow guns (tools) to be used for their normal purpose
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		if (player.getItemInHand() == null)
			return;
		
		player.getItemInHand().setDurability((short) 0);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerHitEntity(EntityDamageByEntityEvent event) { // don't let knifes (swords) to be damaged
		if (!(event.getDamager() instanceof Player))
			return;
		
		Player player = (Player) event.getDamager();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		if (player.getItemInHand() == null)
			return;
		
		player.getItemInHand().setDurability((short) 0);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) { // don't let players get hungry
		event.setCancelled(true);
	}
	
}
