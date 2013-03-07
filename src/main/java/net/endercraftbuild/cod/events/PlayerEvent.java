package net.endercraftbuild.cod.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
 
public abstract class PlayerEvent extends Event {
	
    private final Player player;
    
    public PlayerEvent(Player player) {
    	this.player = player;
    }
    
    public String getName() {
    	return player.getName();
    }
    
    public Player getPlayer() {
    	return player;
    }
    
}