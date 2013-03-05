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
	private final ZombieGame game;

	public EntityBarrierDamageListener(CoDMain plugin, ZombieGame game) {
		this.plugin = plugin;
		this.game = game;
	}
	
	@EventHandler
	public void onGameTick(GameTickEvent event) {
		if (event.getGame() != game)
			return;
		
		for (Barrier barrier : game.getBarriers())
			for (GameEntity gameEntity : game.getGameEntities())
				if (gameEntity.near(barrier.getLocation())) {
					barrier.damage();
					break;
				}
	}

}
