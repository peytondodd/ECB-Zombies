package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;
import de.ntcomputer.minecraft.controllablemobs.api.ai.behaviors.AITargetNearest;
import de.ntcomputer.minecraft.controllablemobs.api.ai.EntityFilter;

public abstract class GameEntity implements EntityFilter {
	private static final int NEAR = 3;
	
	private final ZombieGame game;
	private final Spawner spawner;
	
	public GameEntity(ZombieGame game, Spawner spawner) {
		this.game = game;
		this.spawner = spawner;
	}
	
	public ZombieGame getGame() {
		return game;
	}
	
	public Spawner getSpawner() {
		return spawner;
	}
	
	public boolean near(Location target) {
		Location location = getMob().getEntity().getLocation();
		
		if (location.getBlockY() != target.getBlockY())
			return false;
		
		if (location.distance(target) > NEAR)
			return false;
		
		return true;
	}
	
	public boolean isEntityValid(Entity target) {
		if (!(target instanceof Player))
			return false;
		Player player = (Player) target;
		return game.isInGame(player);
	}
	
	protected void enrage() {
		ControllableMob<?> mob = getMob();
		
		// FIXME(mortu): runner code isn't changing speed ... will fix later
		/*
		if (game.shouldSpawnRunner()) {
			mob.getProperties().setMovementSpeed(2.0f);
			game.broadcast("I'm coming for you!");
		} else {
			mob.getProperties().setMovementSpeed(0.25f);
			game.broadcast("I'll get there one day!");
		}
		*/
		
		mob.getAI().addAIBehavior(new AITargetNearest(10, 0, false, 0, this));

		Creature creature = (Creature) mob.getEntity();
		creature.setMaxHealth(creature.getMaxHealth() + game.getCurrentWave().intValue() - 1);
		creature.setHealth(creature.getMaxHealth());
	}
	
	public abstract ControllableMob<?> getMob();
	protected abstract void spawnMob();
	
	public void spawn() {
		spawnMob();
		enrage();
	}

	public void respawn() {
		despawn();
		spawn();
	}
	
	public void despawn() {
		Entity entity = getMob().getEntity();
		ControllableMobs.unassign(getMob());
		entity.remove();
	}
	
}
