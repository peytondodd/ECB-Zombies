package net.endercraftbuild.zombies.customevents;

import net.endercraftbuild.zombies.games.Game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class GameEndEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    
    private Game game;
    
    //Check if game is zombies or PvP
    
    public String getName() {
    	return game.getName();
    }
    
    public Game getGame() {
    	return game;
    }
 
    public void setRound(Long round) {
    	game.setCurrentWave(round);
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}