package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class PlayerJoinEvent extends GameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    
    public PlayerJoinEvent(Player player, Game game) {
    	super(game);
    	this.player = player;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}