package main.java.net.endercraftbuild.zombies.utils;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Utils {

	public static ArrayList<String> Playing = new ArrayList<String>();


	public static boolean isInGame(Player player) {
		String playerName = player.getName();
		if(Playing.contains(playerName)) {
			return true;
		}
		else
			return false;
	}
	public static void setInGame(Player player) {
		String playerName = player.getName();
		if(isInGame(player)== false) 
		{
			Playing.add(playerName);
		}
	}
}