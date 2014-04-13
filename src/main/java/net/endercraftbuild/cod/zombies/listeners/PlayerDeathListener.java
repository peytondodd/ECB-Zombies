package net.endercraftbuild.cod.zombies.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.events.PlayerDiedEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.PlayerReviveEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.objects.DeadPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class PlayerDeathListener implements Listener {

	private final CoDMain plugin;
	private final ZombieGame game;
	private final Map<Player, DeadPlayer> deadPlayers;
	
	public PlayerDeathListener(CoDMain plugin, ZombieGame game) {
		this.plugin = plugin;
		this.game = game;
		this.deadPlayers = new HashMap<Player, DeadPlayer>();
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveEvent event) {
		if (event.getGame() != game)
			return;
		
		Player player = event.getPlayer();
		
		if (deadPlayers.containsKey(player))
			deadPlayers.remove(player);
		
		// TODO(mortu): move player count into event, setting oldPlayerCount and newPlayerCount 
		if ((game.getPlayers().size() - 1) == deadPlayers.size())
			safelyEndGame();
	}
	
	@EventHandler //Event called when a nametag is rendered
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		if (deadPlayers.containsKey(event.getNamedPlayer())) { //Check if the player whos name needs to be changed is downed.
			DeadPlayer dp = deadPlayers.get(event.getNamedPlayer());
			event.setTag(ChatColor.DARK_RED + "DOWNED! CLICK TO REVIVE: " + ChatColor.DARK_GREEN + dp.getTimeRemaining());
		
		}
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		Iterator<DeadPlayer> iterator = deadPlayers.values().iterator();
		while (iterator.hasNext()) {
			DeadPlayer deadPlayer = iterator.next();
			if (deadPlayer.getTimeRemaining() == 1)
				
				deadPlayer.spawn();
				
			else
				deadPlayer.update();
		}
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		if (event.getGame() != game)
			return;
		
		for (DeadPlayer deadPlayer : deadPlayers.values()) {
			
			deadPlayer.spawn();
		}
		
		deadPlayers.clear();
	}
	
	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		if (event.getGame() != game)
			return;
		
		for (DeadPlayer deadPlayer : deadPlayers.values())
			deadPlayer.respawn();
		deadPlayers.clear();
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDiedEvent event) {
		if (event.getGame() != this.game)
			return;
		
		Player player = event.getPlayer();

		if (deadPlayers.containsKey(player))
			return;
		
		DeadPlayer deadPlayer = new DeadPlayer(player, this.game);
		deadPlayers.put(player, deadPlayer);
		deadPlayer.downPlayer();
		
		game.broadcast(ChatColor.BOLD.toString() + ChatColor.DARK_RED + event.getDeathMessage());
		
		if (deadPlayers.size() == game.getPlayers().size())
			safelyEndGame();
	}


	//@EventHandler - OLD sign revive
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.SIGN_POST)
			return;
		
		Sign sign = (Sign) block.getState();
		if (sign.getLine(0) != DeadPlayer.SIGN_HEADER)
			return;
		
		Iterator<DeadPlayer> iterator = deadPlayers.values().iterator();
		while (iterator.hasNext()) {
			DeadPlayer deadPlayer = iterator.next();
			if (!deadPlayer.getPlayer().getName().startsWith(ChatColor.stripColor(sign.getLine(1))))
				continue;
			deadPlayer.revive();
			iterator.remove();
			game.callEvent(new PlayerReviveEvent(deadPlayer.getPlayer(), event.getPlayer()));
		}
	}
	@EventHandler
	public void reviveInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if(!(event.getRightClicked() instanceof Player)) 
			return;
		
		Player interacted = (Player) event.getRightClicked();
		if(deadPlayers.containsKey(interacted)) {
			deadPlayers.get(interacted).revive();
			deadPlayers.remove(interacted);
			TagAPI.refreshPlayer(interacted);
			game.callEvent(new PlayerReviveEvent(interacted, player));
			
			
			
		}
		
		
		
	}
	@EventHandler //Zombies wont target dead players
	public void onZombieTarget(EntityTargetEvent event) {

		if(event.getEntity() instanceof Zombie && event.getTarget() instanceof Player) {
			Player target = (Player) event.getTarget();
			
			if(plugin.getGameManager().get(target) == null)
				return;
			
			
			if(deadPlayers.containsKey(target)) {
				event.setCancelled(true);

			}
		}
	}

	private void safelyEndGame() {
		// XXX(mortu): the delay is to keep MC and ControllableMobsAPI from fighting over the mob 
		new BukkitRunnable() {
			@Override
			public void run() {
				game.stop();
			}
		}.runTask(plugin);
	}
	
}
