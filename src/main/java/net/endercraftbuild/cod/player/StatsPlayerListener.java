package net.endercraftbuild.cod.player;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.PlayerReviveEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by CP on 11/30/2014.
 */
public class StatsPlayerListener implements Listener {

    private final CoDMain plugin;

    public StatsPlayerListener(CoDMain plugin) {
        this.plugin = plugin;
    }




    @EventHandler
    public void onRoundAdvance(RoundAdvanceEvent event) {
       ZombieGame game = (ZombieGame)event.getGame();

       if(game.getCurrentWave() == 1)
           return;

       for (Player players : game.getPlayers()) {
           CoDPlayer cp = plugin.getPlayerManager().getPlayer(players);
           cp.setRoundsSurvived(cp.getRoundsSurvived() + 1);
           cp.giveXp(20);
        }
    }

    @EventHandler
    public void onRevive(PlayerReviveEvent event) {

        CoDPlayer reviver = plugin.getPlayerManager().getPlayer(event.getRevivedBy());
        reviver.incrementRevives();
        reviver.giveXp(10);

    }

    /*
    * -- Door tracking --
    * Door stats tracking is in the door open code in zombies>listeners>DoorSignListener
    * Don't want to make a new event just for this
    *
    */





    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        CoDPlayer p = plugin.getPlayerManager().getPlayer(event.getPlayer());
        if(p == null) { //hopefully we never see this
            event.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Error!" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + event.getFormat());
            return; //prevent error
        }
        if(p.getLevel() <= 5) {
            event.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Lv. " + p.getLevel() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + event.getFormat());
        } else if(p.getLevel() > 5 && p.getLevel() <= 10) {
            event.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Lv. " + p.getLevel() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + event.getFormat());
        } else {
            event.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Lv. " + p.getLevel() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + event.getFormat());
        }
    }
}



