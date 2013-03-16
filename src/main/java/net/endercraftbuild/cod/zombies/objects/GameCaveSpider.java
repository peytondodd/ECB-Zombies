package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;

public class GameCaveSpider extends GameEntity {

	private ControllableMob<CaveSpider> cavespider;

	public GameCaveSpider(ZombieGame game, Spawner spawner) {
		super(game, spawner);
	}

	@Override
	public ControllableMob<?> getMob() {
		return cavespider;
	}

	@Override
	public void spawnMob() {
		this.cavespider = ControllableMobs.assign((CaveSpider) getGame().getSpawnLocation().getWorld().spawnEntity(getSpawner().getLocation(), EntityType.CAVE_SPIDER));
		this.cavespider.getEntity().setFireTicks(Integer.MAX_VALUE);
	}

}
