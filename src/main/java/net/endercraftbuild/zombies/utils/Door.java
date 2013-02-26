package net.endercraftbuild.zombies.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Door
{
	public Location loc;
	private Location top;
	public int status = 0;
	public BlockFace f = BlockFace.NORTH;
	
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
	
	public void buildDoor()
	{
		loc.getBlock().setTypeIdAndData(Material.WOODEN_DOOR.getId(), (byte)~0x8, false);
		top.getBlock().setTypeIdAndData(Material.WOODEN_DOOR.getId(), (byte)0x8, false);
		switch(f)
		{
		case NORTH:
			loc.getBlock().setData((byte)0x1, false);
			top.getBlock().setData((byte)0x1, false);
			break;
		case SOUTH:
			loc.getBlock().setData((byte)0x3, false);
			top.getBlock().setData((byte)0x3, false);
			break;
		case EAST:
			loc.getBlock().setData((byte)0x2, false);
			top.getBlock().setData((byte)0x2, false);
			break;
		case WEST:
			loc.getBlock().setData((byte)0x0, false);
			top.getBlock().setData((byte)0x0, false);
			break;
		}
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
