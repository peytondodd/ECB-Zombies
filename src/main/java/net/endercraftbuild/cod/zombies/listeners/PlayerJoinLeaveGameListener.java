package net.endercraftbuild.cod.zombies.listeners;

import java.util.List;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerJoinLeaveGameListener implements Listener {

	private final CoDMain plugin;
	
	public PlayerJoinLeaveGameListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ZombieGame game = (ZombieGame) event.getGame();
		reset(player);
		player.teleport(game.getSpawnLocation());
		//kits
		if(player.hasPermission("ecb.cod.donorkit")) 
		{
		List<Integer> dkit = plugin.getConfig().getIntegerList("kits.items.donor");
		PlayerInventory inv = player.getInventory();
		for (Integer id : dkit) {
		ItemStack givedonorkit = new ItemStack(id, 1);
		ItemStack ammo = new ItemStack(337, 64);//Just plain give ammo by default, too lazy to add multiple item config
		inv.addItem(givedonorkit);
		ammo = Utils.setItemName(ammo, "Bullets");//rename to bullets!
		inv.addItem(ammo);
			}
		}
		else 
			{
			List<Integer> kit = plugin.getConfig().getIntegerList("kits.items.normal");
			PlayerInventory inv = player.getInventory();
			for (Integer id : kit) {
			ItemStack givekit = new ItemStack(id, 1);
			ItemStack ammo = new ItemStack(337, 64);//Just plain give ammo by default, too lazy to add multiple item config
			ammo = Utils.setItemName(ammo, "Bullets");
			inv.addItem(ammo);
			inv.addItem(givekit);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerLeaveEvent event) {
		Player player = event.getPlayer();
		reset(player);
		player.teleport(player.getWorld().getSpawnLocation());
	}
	
	private void reset(Player player) {
		player.setExp(0);
		player.getInventory().clear();
		plugin.getEconomy().withdrawPlayer(player.getName(), plugin.getEconomy().getBalance(player.getName()));
	}
}
