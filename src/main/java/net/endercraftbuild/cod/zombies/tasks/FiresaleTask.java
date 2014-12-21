package net.endercraftbuild.cod.zombies.tasks;

import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by CP on 12/7/2014.
 */
public class FiresaleTask extends BukkitRunnable {

    private ZombieGame game;

    public FiresaleTask(ZombieGame game) {
        this.game = game;
    }

    //a task to simply stop a fire sale (ran as a 300 tick delay)
    @Override
    public void run() {
        game.setFireSale(false);
        //announce players
        game.broadcast(ChatColor.RED.toString() + ChatColor.BOLD + "--- FIRESALE OVER ---");
        this.cancel();
    }
}
