package net.endercraftbuild.cod.commands;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.player.CoDPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by CP on 12/7/2014.
 */
public class SetPlayerLevelCommand implements CommandExecutor {

    private CoDMain plugin;

    public SetPlayerLevelCommand(CoDMain plugin) {
        this.plugin = plugin;
    }

    // /setlevel (player) (level)
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        if(!sender.hasPermission("cod.admin.setlevel"))
            return true;
        if (args.length < 2)
            return false;


        Player player = (Player) sender;

        try {
            CoDPlayer cp = plugin.getPlayerManager().getPlayer(plugin.getServer().getPlayer(args[0]));
            cp.setLevel(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + e.getLocalizedMessage());
            return true;
        }

        return true;
    }

}