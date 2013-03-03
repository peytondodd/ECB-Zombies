package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.games.Game;

import org.bukkit.event.HandlerList;
 
public class GameEndEvent extends GameEvent {
	
	private static final HandlerList handlers = new HandlerList();
    
    public GameEndEvent(Game game) {
		super(game);
	}
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}