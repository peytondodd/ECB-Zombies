package net.endercraftbuild.cod.tasks;

import net.endercraftbuild.cod.CoDMain;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class PerpetualNightTask extends BukkitRunnable {
	
	private final CoDMain plugin;
	
	public PerpetualNightTask(CoDMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (World world : plugin.getServer().getWorlds())
			world.setTime(13000L);
	}
	
	public void start() {
		runTaskTimer(plugin, 60L, 8000L);
	}
	
	public void stop() {
		cancel();
	}

}
