package net.endercraftbuild.cod.zombies.events;

import net.endercraftbuild.cod.games.ZombieGame;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class RoundAdvanceEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    
    private ZombieGame game;

    public Long getRound() {
        return game.getCurrentWave();
    }
    
    public String getName() {
    	return game.getName();
    }
    
    public ZombieGame getGame() {
    	return game;
    }
 
    public void setRound(Long round) {
    	game.setCurrentWave(round);
    }

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
    
}