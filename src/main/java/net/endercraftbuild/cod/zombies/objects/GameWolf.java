package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class GameWolf extends GameEntity {
	
	private Wolf wolf;
	
	public GameWolf(ZombieGame game, Spawner spawner) {
		super(game, spawner);
	}
	
	@Override
	public Wolf getMob() {
		return wolf;
	}
	
	@Override
	public void spawnMob() {
		this.wolf = (Wolf) getGame().getSpawnLocation().getWorld().spawnEntity(getSpawner().getLocation(), EntityType.WOLF);
		
		this.wolf.setFireTicks(Integer.MAX_VALUE);
		this.wolf.setAngry(true);
	}
	
}
