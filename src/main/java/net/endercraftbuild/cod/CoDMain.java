package net.endercraftbuild.cod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.endercraftbuild.cod.GameManager;
import net.endercraftbuild.cod.commands.*;
import net.endercraftbuild.cod.guns.Shoot;
import net.endercraftbuild.cod.listeners.*;
import net.endercraftbuild.cod.zombies.commands.*;
import net.endercraftbuild.cod.zombies.listeners.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CoDMain extends JavaPlugin {
	
	public String prefix = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Zombies" + ChatColor.GREEN + "> ";
	
	
	private Economy economy;
	
	private GameManager gameManager;
	
	public List<String> pistol = new ArrayList<String>();
	public List<String> reloaders = new ArrayList<String>();
	public List<String> reloadersRocket = new ArrayList<String>();
	public List<String> reloading = new ArrayList<String>();
	public HashMap<String, Integer> kills = new HashMap<String, Integer>();
	
	public void onEnable() {
		//if(!(getServer().getIp() == "127.0.0.1"))
			//setEnabled(false);
		//else
			//Will add in final release! Restrict the plugin for ECB only
		
		if (!new File(this.getDataFolder().getPath() + File.separatorChar + "config.yml").exists())
			saveDefaultConfig();
		
		if (!setupEconomy())
			getLogger().warning("Vault not found!");
		

		
		registerDynamicPermissions();
		registerListeners();
		registerCommands();
		setupGameManager();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	public void onDisable() {
		getGameManager().disable();
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
		
		getServer().getPluginManager().registerEvents(new GlobalMechanicsListener(this), this);
		getServer().getPluginManager().registerEvents(new ZombieGameMechanicsListener(this), this);
		getServer().getPluginManager().registerEvents(new MysterBoxListener(this), this);
		getServer().getPluginManager().registerEvents(new Shoot(this), this);  // FIXME: should be called ShootListener
		getServer().getPluginManager().registerEvents(new WallWeaponSignListener(this), this);
		getServer().getPluginManager().registerEvents(new MiscListener(this), this);
		//getServer().getPluginManager().registerEvents(new PVPJoinLeaveListener(this), this);
	}
	
	private void registerCommands() {
		getCommand("join").setExecutor(new JoinCommand(this));
		getCommand("leave").setExecutor(new LeaveCommand(this));
		getCommand("stats").setExecutor(new StatsCommand(this));
		
		getCommand("texture").setExecutor(new TextureCommand(this));
		getCommand("tpack").setExecutor(new TextureCommand(this));
		// generic admin commands
		getCommand("csave").setExecutor(new SaveCommand(this));
		getCommand("creload").setExecutor(new ReloadCommand(this));
		getCommand("cstart").setExecutor(new StartCommand(this));
		getCommand("cstop").setExecutor(new StopCommand(this));
		getCommand("cremove").setExecutor(new RemoveCommand(this));
		getCommand("ctoggle").setExecutor(new ToggleCommand(this));
		
		// zombie admin commands
		getCommand("zcreate").setExecutor(new CreateCommand(this));
		getCommand("zsendallhub").setExecutor(new SendAllHub(this));
		getCommand("zedit").setExecutor(new EditCommand(this));
		getCommand("zmob").setExecutor(new MobCommand(this));
		getCommand("zspawn").setExecutor(new SpawnerCommand(this));
		getCommand("zbarrier").setExecutor(new BarrierCommand(this));
		getCommand("zdoor").setExecutor(new DoorCommand(this));
		getCommand("zlink").setExecutor(new LinkCommand(this));
		getCommand("zlinkclear").setExecutor(new LinkClearCommand(this));
		getCommand("zlobby").setExecutor(new LobbyCommand(this));
		getCommand("zadvance").setExecutor(new AdvanceCommand(this));
		
		getCommand("setlobbysign").setExecutor(new LobbySignCommand(this));
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
	
}


