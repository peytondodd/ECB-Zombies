package net.endercraftbuild.cod.zombies.events;

import net.endercraftbuild.cod.events.GameEvent;
import net.endercraftbuild.cod.games.ZombieGame;

import org.bukkit.event.HandlerList;
 
public class RoundAdvanceEvent extends GameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    public RoundAdvanceEvent(ZombieGame game) {
    	super(game);
    }

    public Long getRound() {
        return ((ZombieGame) getGame()).getCurrentWave();
    }
     
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}