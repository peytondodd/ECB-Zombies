package net.endercraftbuild.cod.zombies.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.endercraftbuild.cod.CoDMain;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
			deadPlayers.remove(player).removeSign();
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		Iterator<DeadPlayer> iterator = deadPlayers.values().iterator();
		while (iterator.hasNext()) {
			DeadPlayer deadPlayer = iterator.next();
			if (deadPlayer.isExpired())
				deadPlayer.removeSign();
			else
				deadPlayer.updateSign();
		}
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
		deadPlayer.spawn();
		
		// XXX(mortu): the delay is to keep MC and ControllableMobsAPI from fighting over the mob 
		if (deadPlayers.size() == game.getPlayers().size())
			new BukkitRunnable() {
				@Override
				public void run() {
					game.stop();
				}
			}.runTask(plugin);
	}
	
	@EventHandler
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
	
}
