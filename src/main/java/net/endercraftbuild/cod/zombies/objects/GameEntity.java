package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			return false;
		else
			return game.isInGame(player);
	}
	
	protected void enrage() {
		ControllableMob<?> mob = getMob();
		mob.getAI().addBehavior(new AITargetNearest(10, 0, false, 0, this));
		
		// NOTE(mortu): proof of concept ... makes it take about 600 shots from AK-47 to kill 4 zombies
		Damageable damageable = (Damageable) mob.getEntity();
		damageable.setMaxHealth(500);
		damageable.setHealth(damageable.getMaxHealth());
		
		// NOTE(mortu): proof of concept ... makes the zombies zoom around faster than any player can run
		mob.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5, true), true);
	}
	
	public abstract ControllableMob<?> getMob();
	protected abstract void spawnMob();
	
	public void spawn() {
		spawnMob();
		enrage();
	}

	@SuppressWarnings("deprecation")
	public void respawn() {
		Entity entity = getMob().getEntity();
		
		if(ControllableMobs.isAssigned((LivingEntity) entity))
			despawn();
		
		spawn();
	}
	
	@SuppressWarnings("deprecation")
	public void despawn() {
		Entity entity = getMob().getEntity();
		
		if(ControllableMobs.isAssigned((LivingEntity) entity))
			ControllableMobs.unassign(getMob());
	
		entity.remove();
	}
}
