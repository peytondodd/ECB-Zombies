package net.endercraftbuild.cod.tasks;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.events.GameTickEvent;

import org.bukkit.scheduler.BukkitRunnable;

public class GameTickTask extends BukkitRunnable {
	
	private final CoDMain plugin;
	
	public GameTickTask(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Game game : plugin.getGameManager().getActiveGames())
			game.callEvent(new GameTickEvent(game));
	}
	
	public void start() {
		runTaskTimer(plugin, 20L, 20L);
	}
	
	public void stop() {
		cancel();
	}

}
