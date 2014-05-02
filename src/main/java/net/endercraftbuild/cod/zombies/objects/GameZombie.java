package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Zombie;

public class GameZombie extends GameEntity {
	
	private Zombie zombie;
	
	public GameZombie(ZombieGame game, Spawner spawner) {
		super(game, spawner);
	}
	
	@Override
	public Zombie getMob() {
		return zombie;
	}
	
	@Override
	protected void spawnMob() {
		
		this.zombie = getGame().getSpawnLocation().getWorld().spawn(getSpawner().getLocation(), Zombie.class);
	}
	
}
