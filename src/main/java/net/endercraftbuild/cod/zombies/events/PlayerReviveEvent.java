package net.endercraftbuild.cod.zombies.events;

import net.endercraftbuild.cod.events.PlayerEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class PlayerReviveEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Player revivedBy;
	
    public PlayerReviveEvent(Player player, Player revivedBy) {
		super(player);
		this.revivedBy = revivedBy;
	}
    
	public Player getRevivedBy() {
		return revivedBy;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}