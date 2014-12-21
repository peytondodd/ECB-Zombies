package net.endercraftbuild.cod.zombies.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.Game;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by CP on 12/7/2014.
 */
public class MinLevelCommand implements CommandExecutor {

    private CoDMain plugin;

    public MinLevelCommand(CoDMain plugin) {
        this.plugin = plugin;
    }

    // /zminlevel <name> <level>
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        if(!sender.hasPermission("cod.admin.minlevel")) //same perm idc
            return true;
        if (args.length < 2)
            return false;

        Player player = (Player) sender;

        try {
            Game game =  plugin.getGameManager().get(args[0]);
            game.setMinLevel(Integer.parseInt(args[1]));
            player.sendMessage(plugin.prefix + "Minimum level for " + game.getName() + " set to " + game.getMinLevel() + " ! Use /csave");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
            return true;
        }

        return true;
    }

}

