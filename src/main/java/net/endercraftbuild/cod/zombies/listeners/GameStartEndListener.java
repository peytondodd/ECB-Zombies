package net.endercraftbuild.cod.zombies.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameEndEvent;
import net.endercraftbuild.cod.events.GameStartEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;

public class GameStartEndListener implements Listener {
	
	private final CoDMain plugin;

	public GameStartEndListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		game.closeDoors();
		game.rebuildBarriers();
		for (Player player : game.getPlayers())
			plugin.getServer().getPluginManager().callEvent(new PlayerJoinEvent(player, game));
		game.activateSpawners();
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		game.deactivateSpawners();
		game.rebuildBarriers();
		game.closeDoors();
		for (Player player : game.getPlayers())
			game.removePlayer(player);
	}
	
}
