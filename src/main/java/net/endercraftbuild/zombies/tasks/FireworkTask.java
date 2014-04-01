package net.endercraftbuild.zombies.tasks;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.utils.FireworkEffectPlayer;
import net.endercraftbuild.cod.zombies.ZombieGame;

public class FireworkTask extends BukkitRunnable {

	private final CoDMain plugin;
	private final ZombieGame game;
	private final FireworkEffectPlayer fireworks;
	private final List<Location> blocks;
	
	public FireworkTask(ZombieGame game, CoDMain plugin) {
		this.plugin = plugin;
		this.game = game;
		this.fireworks = new FireworkEffectPlayer();
		this.blocks = fireworks.circle(game.getSpawnLocation(), 10, 1, true, false, 9);
	}

	@Override
	public void run() {

		while (!blocks.isEmpty()) {

			Firework fw = (Firework) game.getSpawnLocation().getWorld().spawnEntity(blocks.get(0), EntityType.FIREWORK);
			FireworkMeta meta = fw.getFireworkMeta();

			meta.addEffect(FireworkEffect.builder().withColor(Color.BLUE).withColor(Color.WHITE).withFade(Color.WHITE).build());

			fw.setFireworkMeta(meta);

			fw.detonate();
			blocks.remove(0);
			//System.out.println("[Zombies Debug] Blocklist size: " + blocks.size() + "  (Game: " + game.getName() + ")");
		}
		if (blocks.isEmpty()) {
			stop();
			
		}
	}

	public void start() {
		runTaskTimer(plugin, 0, 1);
	}

	public void stop() {
		cancel();
		System.out.println("[Zombies Debug] Stopped Firework task. (Game: " + game.getName() + ")");
	}

}
