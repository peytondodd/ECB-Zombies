package net.endercraftbuild.zombies.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Door
{
	public Location loc;
	private Location top;
	public int status = 0;
	
	public void setLocation(Location location)
	{
		loc = location;
		top = loc;
		top.setY(loc.getY()+1);
	}
	
	public void statIncrease()
	{
		if(status == 3 && loc.getBlock().getTypeId() != 0)
		{
			status = 2;
			loc.getWorld().getBlockAt(loc).setTypeId(0);
			loc.getWorld().getBlockAt(top).setTypeId(0);
		}
		if(status == 3 && loc.getBlock().getTypeId() == 0)
		{
			status = 2;
		}
		status += 1;
	}
	
	public boolean checkForZombies()
	{
		List<Entity> ent = new ArrayList<Entity>();
		ent.equals(loc.getWorld().getEntities());
		for(int j = 0; j < ent.size(); j++)
		{
			if(ent.get(j).getLocation().distance(loc) < 4)
			{
				if(ent.get(j).equals(EntityType.ZOMBIE))
				{
					return true;
				}
			}
		}
		return false;
	}
}
