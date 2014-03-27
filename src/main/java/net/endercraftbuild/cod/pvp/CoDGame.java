package net.endercraftbuild.cod.pvp;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.utils.Utils;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CoDGame extends Game {
	

	private Location spawnLocation;
	private Location spawnLocation2;
	private Location lobbyLocation;
	
	public CoDGame(CoDMain plugin) {
		super(plugin);
		
	
	}
	
	@Override
	public void registerListeners() {
		//registerListener(new PVPJoinLeaveListener(getPlugin(), this));
		//registerListener(new PlayerDeathListener(getPlugin(), this));
	}
	
	@Override
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);

		
		setSpawnLocation(Utils.loadLocation(config));

		
		ConfigurationSection lobbyLocationSection = config.getConfigurationSection("lobby-location");
		if (lobbyLocationSection != null)
			setLobbyLocation(Utils.loadLocation(lobbyLocationSection));
		
		return config;
		
		
		}
		
	
	@Override
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection gameSection = super.save(parent);
		
		Utils.saveLocation(getSpawnLocation(), gameSection);
		
		
		if (getLobbyLocation() != null) {
			ConfigurationSection lobbyLocationSection = gameSection.createSection("lobby-location");
			Utils.saveLocation(getLobbyLocation(), lobbyLocationSection);
		}
		return gameSection;
		
	}
	
	@Override
	public void stop() {

		super.stop();
	}
	public void payPlayerPVP(Player player, int amount) {
		Economy economy = getPlugin().getEconomy();
		
		if (economy.depositPlayer(player.getName(), amount).transactionSuccess()) {
			String balance = economy.format(economy.getBalance(player.getName()));
			String deposit = economy.format(amount);
			player.sendMessage(getPlugin().prefix + ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + balance + ChatColor.GREEN + "! Gained: " + ChatColor.DARK_GREEN + deposit + "!");
		}
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public Location getSpawnLocation2() {
		return spawnLocation2;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}
	public void setSpawnLocation2(Location spawnLocation) {
		this.spawnLocation2 = spawnLocation;
	}
	
	public Location getLobbyLocation() {
		return lobbyLocation;
	}
	
	public void setLobbyLocation(Location lobbyLocation) {
		this.lobbyLocation = lobbyLocation;
	}
	
	
	
	public void giveKit(Player player) {
		Utils.giveKit(player, getPlugin().getConfig());
	}	
	
	
	public void payPlayer(Player player, int amount) {
		Economy economy = getPlugin().getEconomy();
		
		if (economy.depositPlayer(player.getName(), amount).transactionSuccess()) {
			String balance = economy.format(economy.getBalance(player.getName()));
			String deposit = economy.format(amount);
			player.sendMessage(getPlugin().prefix + ChatColor.GREEN + "You have " + ChatColor.DARK_GREEN + balance + ChatColor.GREEN + "! Gained: " + ChatColor.DARK_GREEN + deposit + "!");
		}
	}
	
	
}
