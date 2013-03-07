package net.endercraftbuild.cod.zombies.events;

import net.endercraftbuild.cod.events.GameEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.Spawner;

import org.bukkit.event.HandlerList;
 
public class SpawnGameEntityEvent extends GameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    private final Spawner spawner;
    
    public SpawnGameEntityEvent(ZombieGame game, Spawner spawner) {
    	super(game);
    	this.spawner = spawner;
    }
    
    public Spawner getSpawner() {
    	return spawner;
    }
     
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}