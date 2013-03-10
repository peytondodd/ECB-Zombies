package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;

public class GameWolf extends GameEntity {
	
	private ControllableMob<Wolf> wolf;
	
	public GameWolf(ZombieGame game, Spawner spawner) {
		super(game, spawner);
	}
	
	@Override
	public ControllableMob<?> getMob() {
		return wolf;
	}
	
	@Override
	public void spawnMob() {
		this.wolf = ControllableMobs.assign((Wolf) getGame().getSpawnLocation().getWorld().spawnEntity(getSpawner().getLocation(), EntityType.WOLF));
		this.wolf.getEntity().setAngry(true);
		this.wolf.getEntity().setFireTicks(Integer.MAX_VALUE);
	}
	
}
