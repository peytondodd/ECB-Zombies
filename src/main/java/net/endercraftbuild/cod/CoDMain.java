package net.endercraftbuild.cod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.endercraftbuild.cod.commands.*;
import net.endercraftbuild.cod.tasks.GameTickTask;
import net.endercraftbuild.cod.zombies.commands.*;
import net.endercraftbuild.cod.zombies.listeners.*;
import net.endercraftbuild.cod.GameManager;
import net.endercraftbuild.cod.guns.Shoot;
import net.endercraftbuild.cod.listeners.PlayerJoinLeaveSignInteractListener;
import net.endercraftbuild.cod.listeners.PlayerJoinQuitServerListener;
import net.endercraftbuild.cod.listeners.SignAdminListener;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CoDMain extends JavaPlugin {

public String prefix = "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ECB Zombies" + ChatColor.RESET + "] ";

private Economy economy;
private WorldGuardPlugin worldGuard;

private GameManager gameManager;
private GameTickTask gameTickTask;

public List<String> pistol = new ArrayList<String>();
public List<String> reloaders = new ArrayList<String>();
public List<String> reloadersRocket = new ArrayList<String>();
public List<String> reloading = new ArrayList<String>();
public HashMap<String, Integer> kills = new HashMap<String, Integer>();

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
	//if(!(getServer().getIp() == "127.0.0.1"))
		//setEnabled(false);
//	else Will add in final release! Restrict teh plugin for ECB only
	if (!setupEconomy())
		getLogger().warning("Vault not found!");
	
	if (!setupWorldGuard())
		getLogger().warning("WorldGuard not found!");
	registerListeners();
	registerCommands();
	setupGameManager();
	
	gameTickTask = new GameTickTask(this);
	gameTickTask.start();
}

public void onDisable() {
	gameTickTask.stop();
	
	getLogger().info("ECB Zombies disabled");
}

private void registerListeners() {
	getServer().getPluginManager().registerEvents(new SignAdminListener(this), this);
	getServer().getPluginManager().registerEvents(new PlayerJoinQuitServerListener(this), this);
	getServer().getPluginManager().registerEvents(new PlayerJoinLeaveGameListener(this), this);
	getServer().getPluginManager().registerEvents(new PlayerJoinLeaveSignInteractListener(this), this);
	getServer().getPluginManager().registerEvents(new GameStartEndListener(this), this);
	
	getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	getServer().getPluginManager().registerEvents(new BlockListener(this), this);
	getServer().getPluginManager().registerEvents(new PointsListener(this), this);
	getServer().getPluginManager().registerEvents(new Shoot(this), this);  // FIXME: should be called ShootListener
	getServer().getPluginManager().registerEvents(new EntityListener(this), this);
}

private void registerCommands() {
	getCommand("join").setExecutor(new JoinCommand(this));
	getCommand("leave").setExecutor(new LeaveCommand(this));
	
	// generic admin commands
	getCommand("save").setExecutor(new SaveCommand(this));
	
	// zombie admin commands
	getCommand("zcreate").setExecutor(new CreateCommand(this));
	getCommand("zedit").setExecutor(new EditCommand(this));
	getCommand("zspawn").setExecutor(new SpawnerAdminCommand(this));
	getCommand("zsbarrier").setExecutor(new BarrierAdminCommand(this));
}

private boolean setupEconomy() {
	// FIXME: should use the same setup format as worldguard below
	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	if (economyProvider != null) {
		economy = economyProvider.getProvider();
	}

	return economy != null;
}

public Economy getEconomy() {
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


