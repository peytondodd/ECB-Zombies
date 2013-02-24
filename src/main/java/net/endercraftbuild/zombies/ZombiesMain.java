package net.endercraftbuild.zombies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.endercraftbuild.zombies.commands.JoinCommand;
import net.endercraftbuild.zombies.guns.Shoot;
import net.endercraftbuild.zombies.listeners.BlockListener;
import net.endercraftbuild.zombies.listeners.PlayerListener;
import net.endercraftbuild.zombies.listeners.PointsListener;
import net.endercraftbuild.zombies.utils.Door;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ZombiesMain extends JavaPlugin {

public String prefix = "[" + ChatColor.DARK_GREEN + ChatColor.BOLD + "ECB Zombies" + ChatColor.RESET + "] ";

public static Economy economy = null; //Vault for points system

private Shoot Shoot;
protected final YamlConfiguration config = new YamlConfiguration();
public Logger log = Logger.getLogger("Minecraft");
public List<String> pistol = new ArrayList<String>();
public List<String> reloaders = new ArrayList<String>();
public List<String> reloadersRocket = new ArrayList<String>();
public List<String> reloading = new ArrayList<String>();
public HashMap<String, Integer> kills = new HashMap();
public List<Door> doors = new ArrayList<Door>();


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
	getCommand("leave").setExecutor(new JoinCommand(this));
	pm.registerEvents(new Shoot(this), this);
	getLogger().info("ECB Zombies enabled!"); {
	getWorldGuard();

	}
	setupEconomy();
	//Ill import all the doors listed in the config file(as their bottom half) here into the "doors" list(When you guys create the config file)
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
private WorldGuardPlugin getWorldGuard() {
    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

    // WorldGuard may not be loaded
    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
        return null; // Maybe you want throw an exception instead
    }

    return (WorldGuardPlugin) plugin;
}


public void onDisable(){
	getLogger().info("ECB Zombies disabled");
	}

public void reload(final Player player) {
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
    {
      public void run()
      {
        try {
        	
        double payreload = 35.0;
        EconomyResponse r = ZombiesMain.economy.withdrawPlayer(player.getName(), payreload);
		if(!r.transactionSuccess()) 
			{
		  player.sendMessage(prefix + ChatColor.RED + "You need " + payreload + " points to reload!");
		  }
			else {				
          ItemStack stack = new ItemStack(Material.CLAY_BALL, 64);
          player.getInventory().addItem(new ItemStack[] { stack });
          player.sendMessage(ChatColor.GREEN + "Done reloading!");
          reloading.remove(player.getName().toLowerCase());
          player.playSound(player.getLocation(), Sound.CLICK, 160.0F, 80.0F);
          player.updateInventory();
        }
        }
        catch (Exception localException)
        {
        }
      }
    }
    , 100L);
  }

public void DoorChecker()
{
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run()
      {
        try
        {
        	for(int j = 0; j < doors.size(); j++)
        	{
        		if(doors.get(j).checkForZombies())
        		{
        			doors.get(j).statIncrease();
        		}
        	}
        }
        catch (Exception localException)
        {
        }
      }
    }
	
   , 400L, 100L);// every 5 seconds
}

//functions for getting/setting names
public String getItemName(ItemStack is)
{
	ItemMeta im = is.getItemMeta();
	return im.getDisplayName();
}

public ItemStack setItemName(ItemStack is, String str)
{
	ItemMeta im = is.getItemMeta();
	im.setDisplayName(str);
	is.setItemMeta(im);
	return is;
}
}

