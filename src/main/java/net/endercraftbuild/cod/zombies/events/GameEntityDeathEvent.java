package net.endercraftbuild.cod.zombies.events;

import net.endercraftbuild.cod.events.GameEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.objects.GameEntity;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class GameEntityDeathEvent extends GameEvent {
	
    private static final HandlerList handlers = new HandlerList();
    
    private final GameEntity gameEntity;
    private final Player killer;
        
    public GameEntityDeathEvent(ZombieGame game, GameEntity gameEntity, Player killedBy) {
    	super(game);
    	this.gameEntity = gameEntity;
    	this.killer = killedBy;
    }
    
    public GameEntity getGameEntity() {
    	return gameEntity;
    }
    
    public Player getKiller() {
    	return killer;
    }
     
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}