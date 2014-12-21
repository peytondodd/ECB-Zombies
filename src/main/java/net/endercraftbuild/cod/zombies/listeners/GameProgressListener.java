package net.endercraftbuild.cod.zombies.listeners;

import java.util.logging.Level;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.GameEntityDeathEvent;
import net.endercraftbuild.cod.zombies.events.PlayerReviveEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.events.RoundStartEvent;
import net.endercraftbuild.cod.zombies.objects.GameEntity;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

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
		game.healPlayers();
		
		game.broadcastToAll(ChatColor.AQUA + game.getName() + " has started!");
        game.advanceWave();
		game.updateLobbySign();
	}

	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		if (event.getGame() != game)
			return;
		
		game.despawnEntities();
		game.deactivateSpawners();
		game.rebuildBarriers();
		game.closeDoors();
		
		
		game.updateLobbySign();
		
		if(game.isPrivate()) {
			game.broadcastToAll(ChatColor.GREEN + "Donor game: " + game.getName() + " has ended! ");
		}
		else {
			game.broadcastToAll(ChatColor.AQUA + game.getName() + " has ended!");
		}
        for(Player  p : game.getPlayers()) {
            plugin.sendFloatingText(p, ChatColor.DARK_RED + "Game Over", ChatColor.RED +  "You survived " + game.getCurrentWave() + " rounds!");
        }
	}

	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		if (event.getGame() != game)
			return;
		
			
			
		game.rebuildBarriers();
		game.healPlayers();
		game.broadcast(ChatColor.GRAY + "Round " + ChatColor.RED + game.getCurrentWave() + ChatColor.GRAY + " will begin shortly!");
		game.broadcast(ChatColor.GRAY + "There are " + ChatColor.RED + game.getMaxEntityCount() + ChatColor.GRAY + " " + game.getWaveMob() + " in this round!");
		game.updateLobbySign();
		//Effect player
		
}

		
	
	
	@EventHandler
	public void onRoundStart(RoundStartEvent event) {
		if (event.getGame() != game)
			return;
		
		game.broadcast(ChatColor.GRAY + "The round has begun!");

        for(Player p : game.getPlayers()) {
            plugin.sendFloatingText(p, ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Round " + game.getCurrentWave(), ChatColor.RED + "has begun!");
        }
		
		}
	
	
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		game.spawnEntities();
		game.damageBarriers();
		
		for (GameEntity gameEntity : game.getGameEntities())
			if (!gameEntity.getMob().isValid())
				gameEntity.respawn();
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
			Game game = plugin.getGameManager().get(killer);
			if (killer == null || gameEntity.getGame() != game) {
				gameEntity.respawn();
				return;
			}
			
			game.callEvent(new GameEntityDeathEvent(this.game, gameEntity, killer));
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to determine cause of death: ", e);
			gameEntity.respawn();
		}
		
	}
	
	@EventHandler
	public void onGameEntityDeath(GameEntityDeathEvent event) {
		if (event.getGame() != game)
			return;
		
		game.removeGameEntity(event.getGameEntity());
		event.getKiller().giveExp(game.getRandom(25));
		game.payPlayer(event.getKiller(), game.getRandom(30) + 1);
		game.incrementWaveKills();
        //yay randomly drop a perk
        game.randomPerkDrop();
		
		
	}
	

	
	@EventHandler
	public void onPlayerRevive(PlayerReviveEvent event) {
		Player player = event.getPlayer();
		Player revivedBy = event.getRevivedBy();
		Game game = plugin.getGameManager().get(player);
		
		if (game == null || game != this.game)
			return;
		
		game.broadcast(ChatColor.DARK_GREEN + player.getName() + " was revived by " + revivedBy.getName() + "!"); 
		}
	
	
	}


