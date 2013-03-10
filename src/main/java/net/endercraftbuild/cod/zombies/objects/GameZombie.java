package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;

public class GameZombie extends GameEntity {
	
	private ControllableMob<Zombie> zombie;
	
	public GameZombie(ZombieGame game, Spawner spawner) {
		super(game, spawner);
	}
	
	@Override
	public ControllableMob<?> getMob() {
		return zombie;
	}
	
	@Override
	protected void spawnMob() {
		this.zombie = ControllableMobs.assign((Zombie) getGame().getSpawnLocation().getWorld().spawnEntity(getSpawner().getLocation(), EntityType.ZOMBIE));
	}
	
}
