package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

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
		if(!getGame().isAdvanced()) {
            this.zombie = getGame().getSpawnLocation().getWorld().spawn(getSpawner().getLocation(), Zombie.class);
            //make zombie health scale
            this.zombie.setMaxHealth(10 + (getGame().getCurrentWave() * 4));
            this.zombie.setHealth(10 + (getGame().getCurrentWave() * 4));
        } else { //is advanced

            this.zombie = getGame().getSpawnLocation().getWorld().spawn(getSpawner().getLocation(), Zombie.class);
            //advanced is hella harder
            this.zombie.setMaxHealth(13 + (getGame().getCurrentWave() * 5));
            this.zombie.setHealth(13 + (getGame().getCurrentWave() * 5));
            this.zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 2));
        }
	}
	
}
