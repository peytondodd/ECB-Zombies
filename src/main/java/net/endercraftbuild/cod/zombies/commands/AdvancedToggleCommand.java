package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by CP on 12/7/2014.
 */

public class AdvancedToggleCommand implements CommandExecutor {

    private CoDMain plugin;

    public AdvancedToggleCommand(CoDMain plugin) {
        this.plugin = plugin;
    }

    // /ztoggleadvanced (game)
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        if(!sender.hasPermission("cod.admin.toggle"))
            return true;
        if (args.length < 1)
            return false;


        Player player = (Player) sender;

        try {
            player.sendMessage(ChatColor.GREEN + "Game has been " + ( ((ZombieGame) plugin.getGameManager().get(args[0])).toggleAdvanced() ? "toggled to advanced" : "toggled to not advanced") + ".");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
            return true;
        }

        return true;
    }

}
