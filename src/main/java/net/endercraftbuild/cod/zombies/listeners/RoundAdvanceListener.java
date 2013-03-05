package net.endercraftbuild.cod.zombies.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;

public class RoundAdvanceListener implements Listener {
	
	@SuppressWarnings("unused")
	private final CoDMain plugin;
	private final ZombieGame game;

	public RoundAdvanceListener(CoDMain plugin, ZombieGame game) {
		this.plugin = plugin;
		this.game = game;
	}
	
	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		if (event.getGame() != game)
			return;
		
		game.rebuildBarriers();
	}
	
}
