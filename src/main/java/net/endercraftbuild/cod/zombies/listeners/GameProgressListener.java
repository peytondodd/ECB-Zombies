package net.endercraftbuild.cod.zombies.listeners;

import java.util.Iterator;
import java.util.logging.Level;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.GameEntityDeathEvent;
import net.endercraftbuild.cod.zombies.events.PlayerReviveEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.events.SpawnGameEntityEvent;
import net.endercraftbuild.cod.zombies.objects.GameEntity;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameProgressListener implements Listener {

	private final CoDMain plugin;
	private final ZombieGame game;

	public GameProgressListener(CoDMain plugin, ZombieGame game) {
		this.plugin = plugin;
		this.game = game;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		if (event.getGame() != game)
			return;
		
		game.closeDoors();
		game.rebuildBarriers();
		for (Player player : game.getPlayers())
			game.callEvent(new PlayerJoinEvent(player, game));
		game.activateSpawners();
		
		game.broadcastToAll(ChatColor.AQUA + game.getName() + " has started!");
		game.advanceWave();
	}

	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		if (event.getGame() != game)
			return;
		
		Iterator<GameEntity> iterator = game.getGameEntities().iterator();
		while (iterator.hasNext()) {
			GameEntity gameEntity = iterator.next();
			gameEntity.despawn();
			iterator.remove();
		}
		
		game.deactivateSpawners();
		game.rebuildBarriers();
		game.closeDoors();
		
		game.setCurrentWave(0L);
		game.broadcastToAll(ChatColor.AQUA + game.getName() + " has ended!"); 
	}

	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		if (event.getGame() != game)
			return;
		
		game.rebuildBarriers();
		game.broadcast(ChatColor.GRAY + "Round " + ChatColor.RED + game.getCurrentWave() + ChatColor.GRAY + " will begin shortly!");
		game.broadcast(ChatColor.GRAY + "There are " + ChatColor.RED + game.getMaxEntityCount() + ChatColor.GRAY + " zombies in this round!");
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		game.spawnEntities();
		game.damageBarriers();
	}
	
	@EventHandler
	public void onSpawnGameEntity(SpawnGameEntityEvent event) {
		World world = game.getSpawnLocation().getWorld();
		Zombie zombie = (Zombie) world.spawnEntity(event.getSpawner().getLocation(), EntityType.ZOMBIE);
		zombie.setMaxHealth(zombie.getMaxHealth() + game.getCurrentWave().intValue() - 1);
		zombie.setHealth(zombie.getMaxHealth());
		game.addGameEntity(new GameEntity(game, zombie));
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		GameEntity gameEntity = game.findGameEntity(event.getEntity());
		
		if (gameEntity == null)
			return;

		event.getDrops().clear();
		event.setDroppedExp(0);

		try {
			Player killer = event.getEntity().getKiller();
			
			if (killer == null)
				return;
			
			game.callEvent(new GameEntityDeathEvent(game, gameEntity, killer));
			game.incrementWaveKills();
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to determine cause of death: ", e);
		}
		
	}
	
	@EventHandler
	public void onGameEntityDeath(GameEntityDeathEvent event) {
		if (event.getGame() != game)
			return;
		
		game.removeGameEntity(event.getGameEntity());
		event.getKiller().giveExp(game.getRandom(25));
		game.payPlayer(event.getKiller(), game.getRandom(25) + 1);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null || game != this.game)
			return;
		
		game.broadcast(ChatColor.DARK_RED + event.getDeathMessage());
		event.setDeathMessage(null);
	}
	
	@EventHandler
	public void onPlayerRevive(PlayerReviveEvent event) {
		Player player = event.getPlayer();
		Player revivedBy = event.getRevivedBy();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);
		
		if (game == null || game != this.game)
			return;
		
		game.broadcast(ChatColor.DARK_GREEN + player.getName() + " was revived by " + revivedBy.getName() + "!"); 
	}

}
