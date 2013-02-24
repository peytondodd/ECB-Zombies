package net.endercraftbuild.zombies.utils;

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
	public static void setInGame(Player player, boolean bool) {
		String playerName = player.getName();
		if(isInGame(player)== false)
		{
			if(bool == true)
			Playing.add(playerName);
			else
				if(isInGame(player) == true)
				{
					if(bool == false)
					{
						Playing.remove(playerName);
					}
				}
		}
	}
}
