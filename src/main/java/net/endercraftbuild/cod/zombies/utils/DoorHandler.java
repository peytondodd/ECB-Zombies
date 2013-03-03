package net.endercraftbuild.cod.zombies.utils;

import java.util.ArrayList;
import java.util.List;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.zombies.Door;

import org.bukkit.Bukkit;

public class DoorHandler
{
	private CoDMain plugin;
	private List<Door> doors = new ArrayList<Door>();
	
	public void DoorChecker()
	{
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
	    {
	      public void run()
	      {
	        try
	        {
	        	for(int j = 0; j < doors.size(); j++)
	        	{
	        		if(doors.get(j).checkForZombies())
	        		{
	        			doors.get(j).statIncrease();
	        		}
	        	}
	        }
	        catch (Exception localException)
	        {
	        }
	      }
	    }
		
	   , 400L, 100L);// every 5 seconds
	}

	public List<Door> getDoorList()
	{
		return doors;
	}
}
