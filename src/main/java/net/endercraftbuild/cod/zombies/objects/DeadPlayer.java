package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;

public class DeadPlayer {
	
	public static final String SIGN_HEADER = ChatColor.BLUE + "ECB Revive";
	
	private final Player player;
	private final ZombieGame game;
	private final Long diedAt;
	private final ItemStack[] armor;
	private final ItemStack[] inventory;
	
	public DeadPlayer(Player player, ZombieGame game) {
		this.player = player;
		this.game = game;
		this.diedAt = System.currentTimeMillis();
		this.armor = player.getInventory().getArmorContents();
		this.inventory = player.getInventory().getContents();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void downPlayer() {
		
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.setHealth(player.getMaxHealth());
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 5));
		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "BLEEDING OUT IN: " + ChatColor.DARK_GREEN + getTimeRemaining() + "seconds");
		TagAPI.refreshPlayer(getPlayer());
		//downed nametag set via event
		//TODO: Give them a a kit. modify giveKit to give a specific kit from the config
	}
	public boolean isExpired() {
		return (System.currentTimeMillis() - diedAt) > 30 * 1000L;
	}
	public Long getTimeRemaining() {
		return 30 - (System.currentTimeMillis() - diedAt) / 1000L;
		
	}
	
	public void spawn() {
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		
		if (game.getLobbyLocation() != null)
			player.teleport(game.getLobbyLocation());
		else
			player.teleport(game.getSpawnLocation().getWorld().getSpawnLocation());
		
		player.setHealth(player.getMaxHealth());
		player.setFireTicks(0);
		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "You bled out! Wait until next round or type /leave");
		TagAPI.refreshPlayer(player);
		for(PotionEffect effect : player.getActivePotionEffects())
		{
			//Fool died, no perks 4 u!
		    player.removePotionEffect(effect.getType());
		}

	}
	
	
	public void revive() {
		if (isExpired())
			return;
		
		player.getInventory().setArmorContents(armor);
		player.getInventory().setContents(inventory);
		
		player.teleport(game.getSpawnLocation());

		//Revived, don't clear all effects.
		player.removePotionEffect(PotionEffectType.SLOW);
		player.setHealth(player.getMaxHealth());
		TagAPI.refreshPlayer(player);
	}
	
	public void respawn() {
	
		player.teleport(game.getSpawnLocation());
		game.giveKit(player);
		

	}
	
	public void update() {
		if(isExpired())
			return;

		player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED + "BLEEDING OUT IN: " + ChatColor.DARK_GREEN + (30 - (System.currentTimeMillis() - diedAt) / 1000L) + " seconds");
		}
		
	
}
