package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeadPlayer {
	
	public static final String SIGN_HEADER = ChatColor.BLUE + "ECB Revive";
	
	private final Player player;
	private final Sign sign;
	private final ZombieGame game;
	private final Long diedAt;
	private final ItemStack[] armor;
	private final ItemStack[] inventory;
	
	public DeadPlayer(Player player, ZombieGame game) {
		this.player = player;
		this.sign = placeSign();
		this.game = game;
		this.diedAt = System.currentTimeMillis();
		this.armor = player.getInventory().getArmorContents();
		this.inventory = player.getInventory().getContents();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Sign getSign() {
		return this.sign;
	}

	public boolean isExpired() {
		return (System.currentTimeMillis() - diedAt) > 30 * 1000L;
	}
	
	public void spawn() {
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		
		player.teleport(game.getSpawnLocation().getWorld().getSpawnLocation());
		
		player.setHealth(player.getMaxHealth());
		player.setFireTicks(0);
	}
	
	public void revive() {
		if (isExpired())
			return;
		
		removeSign();
		player.getInventory().setArmorContents(armor);
		player.getInventory().setContents(inventory);
		
		Location loc = sign.getLocation();
		Location loc0 = loc;
		loc0.setY(loc0.getY()+1);
		Location loc1 = loc;
		loc1.setY(loc1.getY()+2);
		if (loc0.getBlock().getType() == Material.AIR && loc1.getBlock().getType() == Material.AIR)
			player.teleport(loc0);
		else
			player.teleport(loc);
	}
	
	public void respawn() {
		removeSign();
		player.teleport(game.getSpawnLocation());
		game.giveKit(player);
	}
	
	public void removeSign() {
		if (sign == null)
			return;
		sign.getLocation().getBlock().setType(Material.AIR);
	}

	public void updateSign() {
		if (sign == null)
			return;
		
		Long remaining = 30 - (System.currentTimeMillis() - diedAt) / 1000L;
		ChatColor color = ChatColor.GREEN;
		
		if (remaining < 16)
			color = ChatColor.YELLOW;
		else if (remaining < 5)
			color = ChatColor.RED;
		
		sign.setLine(3, color + remaining.toString());
		sign.update();
	}

	private Sign placeSign() {
		Block block = player.getLocation().getBlock();
		
		if (block.getType() != Material.AIR) {
			player.sendMessage(ChatColor.RED + "Can't place revival sign!");
			return null;
		}
		
		block.setType(Material.SIGN_POST);
		Sign sign = (Sign) block.getState();
		
		sign.setLine(0, SIGN_HEADER);
		sign.setLine(1, ChatColor.RED + player.getName());
		sign.setLine(2, "TIME LEFT");
		sign.setLine(3, ChatColor.GREEN + "30");
		sign.update();
		
		return sign;
	}
	
}
