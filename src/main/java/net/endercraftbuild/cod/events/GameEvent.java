package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.event.Event;
 
public abstract class GameEvent extends Event {
	
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
    
}