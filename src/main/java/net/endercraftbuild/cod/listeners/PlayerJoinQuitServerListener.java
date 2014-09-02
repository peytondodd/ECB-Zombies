package net.endercraftbuild.cod.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

public class PlayerJoinQuitServerListener implements Listener {

	private final CoDMain plugin;
	
	public PlayerJoinQuitServerListener(CoDMain plugin) {
		this.plugin = plugin;
	}


    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(event.getPlayer().getWorld().getSpawnLocation());
        player.getInventory().clear();
        event.setJoinMessage("");
        //score
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("zombies", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "ECB Zombies");

        Score website = objective.getScore(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Website:");
        Score ecb = objective.getScore(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "ecb-mc.net");
        Score blankspace = objective.getScore("  ");

        blankspace.setScore(9);
        website.setScore(7);
        ecb.setScore(6);
        player.setScoreboard(board);

        //score
    }


	@EventHandler
	public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
		Game game = plugin.getGameManager().get(event.getPlayer());
		event.setQuitMessage("");

		if (game != null)
			game.removePlayer(event.getPlayer());

	}
}
