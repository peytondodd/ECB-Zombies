package net.endercraftbuild.zombies.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.customevents.RoundAdvanceEvent;

public class RoundListener implements Listener
{
	private ZombiesMain plugin;

	public RoundListener(ZombiesMain plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onRoundAdvance(RoundAdvanceEvent event)
	{
		for(int i = 0; i < plugin.getDoorList().size(); i++)
		{
			if(plugin.getDoorList().get(i).loc.getBlock().getType() != Material.WOODEN_DOOR)
			{
				plugin.getDoorList().get(i).buildDoor();
			}
		}
	}
}
