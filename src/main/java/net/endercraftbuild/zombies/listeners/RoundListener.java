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
		for(int i = 0; i < event.getGame().getDoorHandler().getDoorList().size(); i++)
		{
			if(event.getGame().getDoorHandler().getDoorList().get(i).loc.getBlock().getType() != Material.WOODEN_DOOR)
			{
				event.getGame().getDoorHandler().getDoorList().get(i).buildDoor();
			}
		}
	}
}
