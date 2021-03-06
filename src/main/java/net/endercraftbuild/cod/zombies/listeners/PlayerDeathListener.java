package net.endercraftbuild.cod.zombies.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.events.PlayerDiedEvent;
import net.endercraftbuild.cod.events.PlayerLeaveGameEvent;
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
import org.bukkit.potion.PotionEffect;
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
	public void onPlayerLeave(PlayerLeaveGameEvent event) {
		if (event.getGame() != game)
			return;
		
		Player player = event.getPlayer();
		
		if (deadPlayers.containsKey(player))
			deadPlayers.remove(player).removeSign();
		
		// TODO(mortu): move player count into event, setting oldPlayerCount and newPlayerCount 
		if ((game.getPlayers().size() - 1) == deadPlayers.size())
			safelyEndGame();
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
	public void onGameEnd(GameEndEvent event) {
		if (event.getGame() != game)
			return;
		
		for (DeadPlayer deadPlayer : deadPlayers.values()) {
			deadPlayer.removeSign();
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
		deadPlayer.spawn();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
		
		game.broadcast(ChatColor.BOLD.toString() + ChatColor.DARK_RED + event.getDeathMessage());

        for(Player p : game.getPlayers()) {
            plugin.sendActionbarMessage(p, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + player.getName() + " has been downed! Go revive them!");
        }

		if (deadPlayers.size() == game.getPlayers().size())
			safelyEndGame();
	}


	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.SIGN_POST)
			return;
		
		Sign sign = (Sign) block.getState();


		if (!sign.getLine(0).equals(DeadPlayer.SIGN_HEADER))
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
