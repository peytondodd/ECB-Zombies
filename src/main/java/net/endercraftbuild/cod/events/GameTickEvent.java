package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.event.HandlerList;
 
public class GameTickEvent extends GameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    public GameTickEvent(Game game) {
    	super(game);
    }
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}