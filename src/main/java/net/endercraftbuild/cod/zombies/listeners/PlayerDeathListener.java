package net.endercraftbuild.cod.zombies.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameTickEvent;
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
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	private final CoDMain plugin;
	private final ZombieGame game;
	private int deadPlayers;
	private final Map<Player, DeadPlayer> inventories;
	private final Map<Sign, DeadPlayer> signs;
	
	public PlayerDeathListener(CoDMain plugin, ZombieGame game) {
		this.plugin = plugin;
		this.game = game;
		this.deadPlayers = 0;
		this.inventories = new ConcurrentHashMap<Player, DeadPlayer>();
		this.signs = new ConcurrentHashMap<Sign, DeadPlayer>(); 
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveEvent event) {
		if (event.getGame() != game)
			return;
		
		Player player = event.getPlayer();
		
		if (inventories.containsKey(player)) {
			signs.remove(inventories.remove(player).getSign()).removeSign();
			deadPlayers--;
		}
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		for (DeadPlayer deadPlayer : inventories.values()) {
			if (deadPlayer.isExpired()) {
				inventories.remove(deadPlayer);
				signs.remove(deadPlayer.getSign());
				deadPlayer.removeSign();
			} else
				deadPlayer.updateSign();
		}
	}
	
	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		if (event.getGame() != game)
			return;
		
		for (DeadPlayer deadPlayer : signs.values())
			deadPlayer.respawn();
		signs.clear();
		inventories.clear();
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

		event.getDrops().clear();
		event.setDroppedExp(0);
		event.setKeepLevel(true);

		if (game == null || game != this.game)
			return;
		if (inventories.containsKey(player))
			return;
		
		try {
			DeadPlayer deadPlayer = new DeadPlayer(player, game);
			inventories.put(player, deadPlayer);
			signs.put(deadPlayer.getSign(), deadPlayer);
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage());
		}
		
		deadPlayers++;
		if (deadPlayers == game.getPlayers().size())
			game.stop();
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.SIGN_POST)
			return;
		
		Sign sign = (Sign) block.getState();
		if (sign.getLine(0) != DeadPlayer.SIGN_HEADER)
			return;
		
		try {
			DeadPlayer deadPlayer = signs.remove(sign);
			inventories.remove(deadPlayer.getPlayer());
			deadPlayer.revive();
			game.callEvent(new PlayerReviveEvent(deadPlayer.getPlayer(), event.getPlayer()));
		} catch (NullPointerException e) {
			// no-op: expired before player was revived
		}
		
		deadPlayers--;
	}
	
}
