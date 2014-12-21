package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class PlayerLeaveGameEvent extends PlayerGameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    public PlayerLeaveGameEvent(Player player, Game game) {
    	super(player, game);
    }
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}