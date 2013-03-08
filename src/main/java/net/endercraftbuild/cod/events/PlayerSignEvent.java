package net.endercraftbuild.cod.events;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
 
public class PlayerSignEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Sign sign;
    
    public PlayerSignEvent(Player player, Sign sign) {
		super(player);
		this.sign = sign;
	}
    
	public Sign getSign() {
		return sign;
	}
	
	public String getLine(int index) {
		return ChatColor.stripColor(sign.getLine(index));
	}
	
	public Double getDouble(int index) {
		return Double.parseDouble(getLine(index).replaceAll("[^\\d\\.-]", ""));
	}
	
	public boolean isJoinSign() {
		return getLine(0).toLowerCase().endsWith("join");
	}
	
	public boolean isWeaponSign() {
		return getLine(0).toLowerCase().endsWith("weapon");
	}
	
	public boolean isAmmoSign() {
		return getLine(0).toLowerCase().endsWith("ammo");
	}
	public boolean isDoorSign() {
		return getLine(0).toLowerCase().endsWith("door");
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
    	return handlers;
    }
    
}