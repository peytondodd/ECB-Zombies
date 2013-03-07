package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class GameEntity {
	private static final int NEAR = 3;
	
	private final ZombieGame game;
	private final Entity entity;
	
	public GameEntity(ZombieGame game, Entity entity) {
		this.game = game;
		this.entity = entity;
	}
	
	public ZombieGame getGame() {
		return game;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public boolean near(Location target) {
		Location location = entity.getLocation();
		
		if (location.getBlockY() != target.getBlockY())
			return false;
		
		if (location.distance(target) > NEAR)
			return false;
		
		return true;
	}
	
	public void despawn() {
		entity.remove();
	}
	
}
