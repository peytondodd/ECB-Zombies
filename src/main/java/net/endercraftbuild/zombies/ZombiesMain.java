package net.endercraftbuild.zombies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.endercraftbuild.zombies.commands.*;
import net.endercraftbuild.zombies.listeners.*;
import net.endercraftbuild.zombies.games.GameManager;
import net.endercraftbuild.zombies.guns.Shoot;
import net.endercraftbuild.zombies.utils.Door;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ZombiesMain extends JavaPlugin {

public String prefix = "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ECB Zombies" + ChatColor.RESET + "] ";

public static Economy economy = null; //Vault for points system
public static WorldGuardPlugin worldGuard = null;

private GameManager gameManager;

private Shoot Shoot;
public Logger log = Logger.getLogger("Minecraft");
public List<String> pistol = new ArrayList<String>();
public List<String> reloaders = new ArrayList<String>();
public List<String> reloadersRocket = new ArrayList<String>();
public List<String> reloading = new ArrayList<String>();
public HashMap<String, Integer> kills = new HashMap();

/*
 * TODO list
 * Put players in games and stuff (Started)
 * Zombie Spawns
 * Zombies breaking down doors (done - geek)
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
	if (!setupEconomy())
		getLogger().warning("Vault not found!");
	
	if (!setupWorldGuard())
		getLogger().warning("WorldGuard not found!");
	
	registerListeners();
	registerCommands();
	setupGameManager();
}

public void onDisable() {
	getLogger().info("ECB Zombies disabled");
}

private void registerListeners() {
	getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	getServer().getPluginManager().registerEvents(new BlockListener(this), this);
	getServer().getPluginManager().registerEvents(new PointsListener(this), this);
	getServer().getPluginManager().registerEvents(new Shoot(this), this);  // FIXME: should be called ShootListener
	getServer().getPluginManager().registerEvents(new EntityListener(this), this);
}

private void registerCommands() {
	getCommand("join").setExecutor(new JoinCommand(this));
	getCommand("leave").setExecutor(new LeaveCommand(this));
	getCommand("create").setExecutor(new CreateCommand(this));
	getCommand("edit").setExecutor(new EditCommand(this));
	getCommand("save").setExecutor(new SaveCommand(this));
}

private boolean setupEconomy() {
	// FIXME: should use the same setup format as worldguard below
	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	if (economyProvider != null) {
		economy = economyProvider.getProvider();
	}

	return (economy != null);
}

public static Economy getEconomy() {
	return economy;
}

private boolean setupWorldGuard() {
    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

    // WorldGuard may not be loaded
    if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        return false;
    
    worldGuard = (WorldGuardPlugin) plugin;

    return true;
}

public WorldGuardPlugin getWorldGuard() {
    return worldGuard;
}

private void setupGameManager() {
	gameManager = new GameManager(this);
	gameManager.load();
}

public GameManager getGameManager() {
	return this.gameManager;
}
//buy ammo from a sign in the spawn of the map
public void reload(final Player player) {
        player.sendMessage(prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
        }
}


