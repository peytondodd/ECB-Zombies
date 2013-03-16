package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.events.PlayerDiedEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;

public class GlobalMechanicsListener implements Listener {

	private final CoDMain plugin;
	
	public GlobalMechanicsListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (player.getGameMode() != GameMode.SURVIVAL)
			player.setGameMode(GameMode.SURVIVAL);
		
		player.setAllowFlight(false);
		player.setFlying(false);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) { // cancel out of game death messages
		event.setDeathMessage(null);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntitySpawn(CreatureSpawnEvent event) { // don't let anything spawn that we didn't spawn
		if (event.getSpawnReason() != SpawnReason.CUSTOM)
			event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onItemUsed(PlayerInteractEvent event) { // don't allow guns (tools) to be used for their normal purpose
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		Player player = event.getPlayer();
		Game game = plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		if (player.getItemInHand() == null)
			return;
		
		Material inHand = player.getItemInHand().getType();
		if (inHand != Material.WOOD_HOE && inHand != Material.STONE_HOE && inHand != Material.IRON_HOE && inHand != Material.DIAMOND_HOE)
			return;
		if (event.getClickedBlock().getType() == Material.DIRT || event.getClickedBlock().getType() == Material.GRASS)
			event.setCancelled(true);
		
		player.getItemInHand().setDurability((short) 0);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerHitEntity(EntityDamageByEntityEvent event) { // don't let knifes (swords) to be damaged
		if (!(event.getDamager() instanceof Player))
			return;
		
		Player player = (Player) event.getDamager();
		Game game = plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		if (player.getItemInHand() == null)
			return;
		
		player.getItemInHand().setDurability((short) 0);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) { // translate lethal damage into a custom PlayerDiedEvent
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player) event.getEntity();
		Game game = plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		
		if ((player.getHealth() - event.getDamage()) <= 0) {
			game.callEvent(new PlayerDiedEvent(player, game, "")); // TODO(mortu): properly determine the killer and set it
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) { // don't let players get hungry
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void preventLandDestructionOnExplode(EntityExplodeEvent event) { // Prevent land damage
		event.blockList().clear();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) { // Prevent players from damaging the map
		Block block = event.getBlock();
		if (block.getType() == Material.SIGN_POST)
			return;
		
		Player player = event.getPlayer();
		
		if (player == null)
			return;
		
		Game game = plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		
		player.sendMessage(ChatColor.RED + "Not allowed!");
		event.setCancelled(true);
	}
	
}
