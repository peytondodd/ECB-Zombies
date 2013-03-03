package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.games.Game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public abstract class GameEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    
    private final Game game;
    
    public GameEvent(Game game) {
    	this.game = game;
    }
    
    public String getName() {
    	return game.getName();
    }
    
    public Game getGame() {
    	return game;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}