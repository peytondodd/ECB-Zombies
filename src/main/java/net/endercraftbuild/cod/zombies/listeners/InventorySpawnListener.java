package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventorySpawnListener implements Listener {

	private final CoDMain plugin;
	
	public InventorySpawnListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		reset(player);
		giveKit(player);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveEvent event) {
		reset(event.getPlayer());
	}
	
	private void reset(Player player) {
		player.setExp(0);
		player.getInventory().clear();
		plugin.getEconomy().withdrawPlayer(player.getName(), plugin.getEconomy().getBalance(player.getName()));
	}
	
	private void giveKit(Player player) {
		PlayerInventory inventory = player.getInventory();
		
		for (String kitName : plugin.getConfig().getConfigurationSection("kits.zombies").getKeys(false)) {
			ConfigurationSection kit = plugin.getConfig().getConfigurationSection("kits.zombies." + kitName);
			if (player.hasPermission("cod." + kit.getCurrentPath())) {
				for (String itemId : kit.getKeys(false)) {
					ConfigurationSection kitItem = kit.getConfigurationSection(itemId);
					ItemStack item = new ItemStack(Integer.parseInt(itemId), kitItem.getInt("quantity", 1));
					if (kitItem.contains("name"))
						Utils.setItemName(item, kitItem.getString("name"));
					inventory.addItem(item);
				}
			}
		}
	}
	
}
