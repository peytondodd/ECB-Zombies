package net.endercraftbuild.cod.zombies.tasks;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireworkTask extends BukkitRunnable {
	private final World world;
	private final List<Location> blocks;
	
	public FireworkTask(World world, List<Location> blocks) {
		this.world = world;
		this.blocks = blocks;
	}

	@Override
	public void run() {
		while (!blocks.isEmpty()) {
			Firework firework = (Firework) world.spawnEntity(blocks.get(0), EntityType.FIREWORK);
			
			FireworkMeta meta = firework.getFireworkMeta();
			meta.addEffect(FireworkEffect.builder().withColor(Color.BLUE).withColor(Color.WHITE).withFade(Color.WHITE).build());
			firework.setFireworkMeta(meta);

			firework.detonate();
			
			blocks.remove(0);
		}
	}
}
