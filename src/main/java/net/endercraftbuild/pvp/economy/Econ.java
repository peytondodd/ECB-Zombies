package net.endercraftbuild.pvp.economy;

import org.bukkit.entity.Player;

public class Econ {

	public double pvppoints = 0.0;

	public void addPoints(Player player, Double amt) {
		//get the players account from database
		pvppoints += amt; //add the amount
		//Update to database
	}

	public void subPoints(Player player, Double amt) {
		//get the players account from database
		pvppoints -= amt; //sub the amount
		//Update to database
	}
}