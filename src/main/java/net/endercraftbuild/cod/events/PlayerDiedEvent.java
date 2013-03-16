package net.endercraftbuild.cod.events;

import net.endercraftbuild.cod.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class PlayerDiedEvent extends PlayerGameEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final String killer;
    
    public PlayerDiedEvent(Player player, Game game, String killer) {
		super(player, game);
		this.killer = killer;
	}
    
    public String getKiller() {
    	return killer;
    }
    
    public String getDeathMessage() { // TODO(mortu): use the killer when it's properly populated
    	return getPlayer().getName() + " died.";
    }
    
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}