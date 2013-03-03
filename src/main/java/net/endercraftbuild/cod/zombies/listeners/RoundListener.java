package net.endercraftbuild.cod.zombies.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;

public class RoundListener implements Listener
{
	private CoDMain plugin;

	public RoundListener(CoDMain plugin)
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
