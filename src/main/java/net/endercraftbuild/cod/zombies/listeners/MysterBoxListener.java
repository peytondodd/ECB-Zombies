package net.endercraftbuild.cod.zombies.listeners;

import java.util.List;
import java.util.Map;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MysterBoxListener implements Listener {

	private CoDMain plugin;

	public MysterBoxListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMysteryBoxUse(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Game game = plugin.getGameManager().get(player);
		
		if (game == null)
			return;
		if (!event.hasBlock())
			return;
		
		Block block = event.getClickedBlock();
		
		if (block.getType() != Material.CHEST)
			return;
		
		Block up   = block.getRelative(BlockFace.UP);
		Block north = up.getRelative(BlockFace.NORTH);
		Block south = up.getRelative(BlockFace.SOUTH);
		Block east  = up.getRelative(BlockFace.EAST);
		Block west  = up.getRelative(BlockFace.WEST);
		
		Sign sign = null;
		
		if (up.getType() == Material.WALL_SIGN)
			sign = (Sign) up.getState();
		else if (north.getType() == Material.WALL_SIGN)
			sign = (Sign) north.getState();
		else if (south.getType() == Material.WALL_SIGN)
			sign = (Sign) south.getState();
		else if (east.getType() == Material.WALL_SIGN)
			sign = (Sign) east.getState();
		else if (west.getType() == Material.WALL_SIGN)
			sign = (Sign) west.getState();
		
		if (sign == null)
			return;
		
		if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("ecb box"))
			return;

		Inventory inventory = ((Chest) block.getState()).getBlockInventory();
		
		if (inventory.getViewers().size() > 0) {
			player.sendMessage(ChatColor.RED + "Only one player at a time!");
			event.setCancelled(true);
			return;
		}
		
		((Chest) block.getState()).getInventory().clear();

		Double cost = Double.parseDouble(sign.getLine(1).replaceAll("[^\\d\\.-]", ""));
		if (!plugin.getEconomy().withdrawPlayer(player.getName(), cost).transactionSuccess()) {
			player.sendMessage(ChatColor.RED + "You do not have enough money!");
			event.setCancelled(true);
			return;
		}
		
		List<Map<?, ?>> items = plugin.getConfig().getMapList("mystery-box");
		Map<?, ?> item = items.get(game.getRandom(items.size()));
		
		ItemStack itemStack = new ItemStack((Integer) item.get("id"), 1);
			
		if (item.containsKey("quantity"))
			itemStack.setAmount((Integer) item.get("quantity"));
		if (item.containsKey("name"))
			Utils.setItemName(itemStack, (String) item.get("name"));
		
		inventory.setContents(new ItemStack[] {itemStack});
	}
	
}
