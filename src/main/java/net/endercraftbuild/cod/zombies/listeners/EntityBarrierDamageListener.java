package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.entities.GameEntity;
import net.endercraftbuild.cod.zombies.objects.Barrier;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityBarrierDamageListener implements Listener {

	@SuppressWarnings("unused")
	private final CoDMain plugin;

	public EntityBarrierDamageListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		for (Barrier barrier : game.getBarriers())
			for (GameEntity gameEntity : game.getGameEntities())
				if (gameEntity.near(barrier.getLocation())) {
					barrier.damage();
					break;
				}
	}

}
