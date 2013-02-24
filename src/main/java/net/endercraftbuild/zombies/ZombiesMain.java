package main.java.net.endercraftbuild.zombies;

import main.java.net.endercraftbuild.zombies.commands.JoinCommand;
import main.java.net.endercraftbuild.zombies.listeners.BlockListener;
import main.java.net.endercraftbuild.zombies.listeners.PlayerListener;
import main.java.net.endercraftbuild.zombies.listeners.PointsListener;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombiesMain extends JavaPlugin {
 
public String prefix = "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ECB Zombies" + ChatColor.RESET + "] ";

public static Economy economy = null; //Vault for points system
/*
 * TODO list
 * Put players in games and stuff (Started)
 * Zombie Spawns
 * Zombies breaking down doors
 * Door Link
 * Mystery Box
 * Wall Weapons (DONE - Peanut)
 * Sign creation (Done - Peanut)
 * Zombie to player ratio
 * Keeping track of rounds + increasing zombie health and numbers on round increase
 * Reward points on Zombie kills (Done, other than guns - Peanut)
 * Game creation
 */

public void onEnable() {
	if (!setupEconomy()) { System.out.println("Warning no economy plugin found!"); }
	PluginManager pm = this.getServer().getPluginManager();
	pm.registerEvents(new PlayerListener(this), this);
	pm.registerEvents(new BlockListener(this), this);
	pm.registerEvents(new PointsListener(this), this);
	getCommand("join").setExecutor(new JoinCommand(this));
	getLogger().info("ECB Zombies enabled!"); {
		
	}
	setupEconomy();
}
private boolean setupEconomy()
{
	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	if (economyProvider != null) {
		economy = economyProvider.getProvider();
	}

	return (economy != null);
}

public static Economy getEconomy() {
	return economy;
}



public void onDisable(){
	getLogger().info("ECB Zombies disabled"); 
	}
}


