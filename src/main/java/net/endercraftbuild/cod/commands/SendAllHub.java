package net.endercraftbuild.cod.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendAllHub implements CommandExecutor{

	private CoDMain plugin;

	public SendAllHub(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	// /sendll
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		if(!sender.hasPermission("cod.admin.sendall")) 
			return true;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try
        {
          out.writeUTF("Connect");
          out.writeUTF("hub");
        }
        catch (IOException eee)
        {
          Bukkit.getLogger().info("You'll never see me!");
        }
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
        	p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        	p.sendMessage(ChatColor.GOLD + "ECB> " + ChatColor.GREEN + "Welcome to hub!");
        	
        }
		return false;
			
				
				
	}			
	
}