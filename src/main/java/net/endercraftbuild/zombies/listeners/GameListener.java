package net.endercraftbuild.zombies.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.endercraftbuild.zombies.ZombiesMain;
import net.endercraftbuild.zombies.customevents.GameEndEvent;

public class GameListener implements Listener
{
	private ZombiesMain plugin;

	public GameListener(ZombiesMain plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void GameEndBuild(GameEndEvent event)
	{
		//Build all doors when game ends
		for(int i = 0; i < event.getGame().getDoorHandler().getDoorList().size(); i++)
		{
			if(event.getGame().getDoorHandler().getDoorList().get(i).loc.getBlock().getType() != Material.WOODEN_DOOR)
			{
				event.getGame().getDoorHandler().getDoorList().get(i).buildDoor();
			}
		}
	}
		@EventHandler
		public void EconReset(GameEndEvent event) {
		{
			//TODO: Getting all players inside a certain game
			//Set their points for zombies to 0
		}
	}
}
