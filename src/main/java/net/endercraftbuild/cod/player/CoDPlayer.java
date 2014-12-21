package net.endercraftbuild.cod.player;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerLevelupEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

/**
 * Created by CP on 11/30/2014.
 */
public class CoDPlayer {

    private Player player;

    private int roundsSurvived;
    private int revives;
    private double exp;
    private int doorsOpened;
    private int weaponsBought;
    private int level = 1;
    private int downs;

    private CoDMain plugin;

    public CoDPlayer(Player player, CoDMain plugin) {
        this.player = player;
        this.plugin = plugin;
    }



    public Player getBukkitPlayer() {
        return player;
    }

    public int getRoundsSurvived() {
        return roundsSurvived;
    }

    public int getRevives() {
        return revives;
    }



    public int getDoorsOpened() {
        return doorsOpened;
    }

    public int getWeaponsBought() {
        return weaponsBought;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return exp;

    }

    public void setRevives(int revives) {
        this.revives = revives;
    }

    public void setXp(double exp) {
        this.exp = exp;
    }

    public void addXp(double exp) {
        this.exp += exp;
        checkLevelup();
    }

    public void setDoorsOpened(int doorsOpened) {
        this.doorsOpened = doorsOpened;
    }

    public void setRoundsSurvived(int roundsSurvived) {
        this.roundsSurvived = roundsSurvived;
    }

    public void setWeaponsBought(int weaponsBought) {
        this.weaponsBought = weaponsBought;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public void incrementDowns() {
        downs++;
    }

    public void incrementRevives() {
        revives++;
    }
    public void giveXp(int exp) {

        //fukkit no dynamic
        if (player.hasPermission("cod.donor.1")) {
            addXp(exp * 1.5);
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "+" + (exp * 1.5) + ChatColor.RED + " XP! (x1.5 Slayer)");
        } else if (player.hasPermission("cod.donor.2")) {
            addXp(exp * 2);
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "+" + exp * 2 + ChatColor.RED + " XP! (x2 Slayer+)");
        } else if (player.hasPermission("cod.donor.3")) {
            addXp(exp * 2.5);
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "+" + (exp * 2.5) + ChatColor.RED + " XP! (x2.5 RANKNAMEHERE)");
        } else {
            addXp(exp);
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "+" + exp + " XP!");
        }
    }

    public void incrementDoors() {
        doorsOpened++;
    }

    public int getNeededToLevelup() {
        return this.getLevel() * this.getLevel() * 35;
    }
    public void checkLevelup() {

        if(this.getLevel() * this.getLevel() * 32 <= this.getXp()) {
            this.level++;
            player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "---------------------------------------------");
            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Level up! Congrats! You are now level " + ChatColor.GOLD +  level);
            player.sendMessage(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "---------------------------------------------");
            PlayerLevelupEvent event = new PlayerLevelupEvent(this, level);
            Bukkit.getServer().getPluginManager().callEvent(event);
            this.checkLevelup(); //see if they leveled up twice same run
        }
    }
    public void payPlayer(Player player, int amount) {
        Economy economy = plugin.getEconomy();

        if (economy.depositPlayer(player.getName(), amount).transactionSuccess()) {
            String balance = economy.format(economy.getBalance(player.getName()));
            String deposit = economy.format(amount);
            player.sendMessage(plugin.prefix + ChatColor.BLUE + "Gained: " + ChatColor.DARK_GREEN + deposit + ChatColor.BLUE + "! Total: " + ChatColor.DARK_GREEN + balance + "!");
        }
    }
}

