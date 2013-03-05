package net.endercraftbuild.cod.zombies.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;

public class RoundAdvanceListener implements Listener {
	
	@SuppressWarnings("unused")
	private final CoDMain plugin;

	public RoundAdvanceListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event) {
		((ZombieGame) event.getGame()).rebuildBarriers();
	}
	
}
