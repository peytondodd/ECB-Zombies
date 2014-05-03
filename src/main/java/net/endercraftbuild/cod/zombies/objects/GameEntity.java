package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffectType;

public abstract class GameEntity {
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
		Location location = getMob().getLocation();
		
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
		
		
	}

	public abstract Entity getMob();

	

	
	protected abstract void spawnMob();
	
	public void spawn() {
		
		spawnMob();
		
	}

	public void respawn() {
		
			
		despawn();
		spawn();
	}
	
	public void despawn() {
		Entity entity = getMob();
		
		entity.remove();
	}
	
}
