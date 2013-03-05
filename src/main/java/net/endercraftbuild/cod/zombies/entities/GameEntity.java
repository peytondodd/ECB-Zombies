package net.endercraftbuild.cod.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class GameEntity {
	private static final int NEAR = 3;
	
	private final Entity entity;
	
	public GameEntity(Entity entity) {
		this.entity = entity;
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
}
