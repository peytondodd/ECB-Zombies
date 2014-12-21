package net.endercraftbuild.cod.zombies.tasks;

import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by CP on 12/8/2014.
 */
public class InstaKillTask extends BukkitRunnable {

    private ZombieGame game;

    public InstaKillTask(ZombieGame game) {
        this.game = game;
    }

    //a task to simply stop a insta kill (600 ticks)
    @Override
    public void run() {
        game.setInstaKill(false);
        //announce players
        game.broadcast(ChatColor.RED.toString() + ChatColor.BOLD + "--- INSTA-KILL OVER ---");
        this.cancel();
    }
}
