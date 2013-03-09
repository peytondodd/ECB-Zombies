package net.endercraftbuild.cod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.endercraftbuild.cod.GameManager;
import net.endercraftbuild.cod.commands.JoinCommand;
import net.endercraftbuild.cod.commands.LeaveCommand;
import net.endercraftbuild.cod.commands.StopCommand;
import net.endercraftbuild.cod.commands.SaveCommand;
import net.endercraftbuild.cod.commands.StartCommand;
import net.endercraftbuild.cod.commands.ReloadCommand;
import net.endercraftbuild.cod.guns.Shoot;
import net.endercraftbuild.cod.listeners.JoinSignListener;
import net.endercraftbuild.cod.listeners.PlayerJoinQuitServerListener;
import net.endercraftbuild.cod.listeners.PlayerSignListener;
import net.endercraftbuild.cod.listeners.SignAdminListener;
import net.endercraftbuild.cod.zombies.commands.BarrierCommand;
import net.endercraftbuild.cod.zombies.commands.CreateCommand;
import net.endercraftbuild.cod.zombies.commands.DoorCommand;
import net.endercraftbuild.cod.zombies.commands.EditCommand;
import net.endercraftbuild.cod.zombies.commands.LinkClearCommand;
import net.endercraftbuild.cod.zombies.commands.LinkCommand;
import net.endercraftbuild.cod.zombies.commands.SpawnerCommand;
import net.endercraftbuild.cod.zombies.listeners.BlockListener;
import net.endercraftbuild.cod.zombies.listeners.DoorSignListener;
import net.endercraftbuild.cod.zombies.listeners.GameMechanicsListener;
import net.endercraftbuild.cod.zombies.listeners.InventorySpawnListener;
import net.endercraftbuild.cod.zombies.listeners.JoinLeaveTeleportListener;
import net.endercraftbuild.cod.zombies.listeners.PlayerListener;
import net.endercraftbuild.cod.zombies.listeners.SignListener;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CoDMain extends JavaPlugin {

public String prefix = "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ECB Zombies" + ChatColor.RESET + "] ";

private Economy economy;
private WorldGuardPlugin worldGuard;

private GameManager gameManager;

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
	//else
		//Will add in final release! Restrict the plugin for ECB only
	if (!new File(this.getDataFolder().getPath() + File.separatorChar + "config.yml").exists())
		saveDefaultConfig();

	if (!setupEconomy())
		getLogger().warning("Vault not found!");
	
	if (!setupWorldGuard())
		getLogger().warning("WorldGuard not found!");
	
	registerDynamicPermissions();
	registerListeners();
	registerCommands();
	setupGameManager();
}

public void onDisable() {
	getGameManager().disable();
	getLogger().info("ECB Zombies disabled");
}

public void reload() {
	reloadConfig();
}

private void registerDynamicPermissions() {
	ConfigurationSection allKits = getConfig().getConfigurationSection("kits");
	for (String gameType : allKits.getKeys(false)) {
		ConfigurationSection gameTypeKits = allKits.getConfigurationSection(gameType);
		for (String kitName : gameTypeKits.getKeys(false)) {
			Permission permission = new Permission("cod.kits." + gameType + "." + kitName, PermissionDefault.FALSE);
			getServer().getPluginManager().addPermission(permission);
		}
	}
}

private void registerListeners() {
	getServer().getPluginManager().registerEvents(new SignAdminListener(this), this);
	getServer().getPluginManager().registerEvents(new PlayerSignListener(this), this);
	getServer().getPluginManager().registerEvents(new PlayerJoinQuitServerListener(this), this);
	getServer().getPluginManager().registerEvents(new JoinLeaveTeleportListener(this), this);
	getServer().getPluginManager().registerEvents(new InventorySpawnListener(this), this);
	getServer().getPluginManager().registerEvents(new JoinSignListener(this), this);
	getServer().getPluginManager().registerEvents(new DoorSignListener(this), this);
	
	getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	getServer().getPluginManager().registerEvents(new BlockListener(this), this);
	getServer().getPluginManager().registerEvents(new Shoot(this), this);  // FIXME: should be called ShootListener
	getServer().getPluginManager().registerEvents(new GameMechanicsListener(this), this);
	getServer().getPluginManager().registerEvents(new SignListener(this), this);
}

private void registerCommands() {
	getCommand("join").setExecutor(new JoinCommand(this));
	getCommand("leave").setExecutor(new LeaveCommand(this));
	
	// generic admin commands
	getCommand("csave").setExecutor(new SaveCommand(this));
	getCommand("creload").setExecutor(new ReloadCommand(this));
	getCommand("cstart").setExecutor(new StartCommand(this));
	getCommand("cstop").setExecutor(new StopCommand(this));
	
	// zombie admin commands
	getCommand("zcreate").setExecutor(new CreateCommand(this));
	getCommand("zedit").setExecutor(new EditCommand(this));
	getCommand("zspawn").setExecutor(new SpawnerCommand(this));
	getCommand("zbarrier").setExecutor(new BarrierCommand(this));
	getCommand("zdoor").setExecutor(new DoorCommand(this));
	getCommand("zlink").setExecutor(new LinkCommand(this));
	getCommand("zlinkclear").setExecutor(new LinkClearCommand(this));
}

private boolean setupEconomy() {
	if (getServer().getPluginManager().getPlugin("Vault") == null)
		return false;

	RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
	if (provider == null)
		return false;

	economy = provider.getProvider();
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
	try {
		gameManager.load();
	} catch (IOException | InvalidConfigurationException e) {
		getLogger().log(Level.SEVERE, "Failed to load games: ", e);
	}
}

public GameManager getGameManager() {
	return this.gameManager;
}

//buy ammo from a sign in the spawn of the map
public void reloadGun(final Player player) {
	player.sendMessage(prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
}

}


