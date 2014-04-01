package net.endercraftbuild.cod.zombies.objects;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Wool;

public class LobbySign extends SerializableGameObject {
	
	private Location location;
	private Sign sign;
	private final CoDMain plugin;
	
	public LobbySign(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	public static final String SIGN_HEADER = ChatColor.BLUE + "ECB Join";
	
	public ConfigurationSection load(ConfigurationSection config) {
		super.load(config);
		
		location = Utils.loadLocation(config);
		
		return config;
	}
	
	public ConfigurationSection save(ConfigurationSection parent) {
		ConfigurationSection spawnerSection = super.save(parent);
		
		Utils.saveLocation(location, spawnerSection);
		
		return spawnerSection;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	private Block getLobbySignBlock() {
		return location.getBlock();
	}
	
	public void show() {
		Wool wool = new Wool();
		wool.setColor(DyeColor.PURPLE);
		location.getBlock().setType(wool.getItemType());
		location.getBlock().setData(wool.getData());
	}
	
	public void hide() {
		location.getBlock().setType(Material.AIR);
	}
	public void rebuild() {
		getLobbySignBlock().setType(Material.WALL_SIGN);
	}
	public void removeSign() {
		if (sign == null)
			return;
		sign.getLocation().getBlock().setType(Material.AIR);
	}

	public void updateSign() {
		
		
		Block block = getLobbySignBlock();
		
		block.setType(Material.WALL_SIGN);
		Sign sign = (Sign) block.getState();
		
		
		ZombieGame game = (ZombieGame) plugin.getGameManager().get(sign.getLine(1));
		int playersongame = game.getPlayers().size();
		ChatColor color = ChatColor.GREEN;
		
		
		sign.setLine(2, ChatColor.BOLD.toString() + color + Integer.toString(playersongame) + "/" + game.getMaximumPlayers());
		
		if(game.isActive()) {
			sign.setLine(3, ChatColor.BOLD.toString() + ChatColor.DARK_GREEN + "Round: " + game.getCurrentWave());
		}
		else if(game.isActive() == false) {
			sign.setLine(3, ChatColor.DARK_RED + "Not Active");
		}
		//sign.setLine(2, "IT WKERS");
		sign.update();
	}

	public void setLine(String string, int line) {
		Block block = getLobbySignBlock();
		
		block.setType(Material.WALL_SIGN);
		Sign sign = (Sign) block.getState();
		sign.setLine(line, string);
		
		
	}
	public Sign placeSign() {
		Block block = getLobbySignBlock();
		
		block.setType(Material.WALL_SIGN);
		Sign sign = (Sign) block.getState();
		
		sign.setLine(0, SIGN_HEADER);

		sign.update();
		
		return sign;
	}
}