package net.endercraftbuild.cod.events;


import net.endercraftbuild.cod.player.CoDPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerLevelupEvent extends Event{

    private static final HandlerList handlers = new HandlerList();
    private final CoDPlayer player;
    private final int newLevel;

    public PlayerLevelupEvent(CoDPlayer player, int newLevel) {
        this.player = player;
        this.newLevel = newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CoDPlayer getPlayer() {
        return player;
    }

    public int getNewLevel() {
        return newLevel;
    }

}
