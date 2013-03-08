package net.endercraftbuild.cod.guns;

import net.endercraftbuild.cod.CoDMain;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Shoot implements Listener {
	
	private final CoDMain plugin;

	public Shoot(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		final Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		
		if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
			int ammoSlot = player.getInventory().first(Material.CLAY_BALL);
			ItemStack ammo = ammoSlot > -1 ? player.getInventory().getItem(ammoSlot) : null;
			
			if (hand.getType() == Material.IRON_HOE) { // AK-47
				if (ammo != null && ammo.getAmount() >= 2) {
					if (ammo.getAmount() == 2)
						player.getInventory().remove(ammo);
					else
						ammo.setAmount(ammo.getAmount() - 2);
					player.updateInventory();
				} else {
					plugin.reload(player);
					return;
				}
				
				player.launchProjectile(Snowball.class);
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
				smokepase(player, loc);
				player.playSound(player.getLocation(), Sound.CLICK, 160.0F, 0.0F);
			
			} else if (hand.getType() == Material.STONE_HOE) { // Shotgun 
				if (this.plugin.reloaders.contains(player.getName())) {
					return;
				}
			
				if (ammo != null && ammo.getAmount() >= 5) {
					if (ammo.getAmount() == 5)
						player.getInventory().remove(ammo);
					else
						ammo.setAmount(ammo.getAmount() - 5);
					player.updateInventory();
				} else {
					plugin.reload(player);
					return;
				}

				this.plugin.reloaders.add(player.getName());
				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
				smokepase(player, loc);
				player.playSound(player.getLocation(), Sound.EXPLODE, 70.0F, 70.0F);
				
				final Shoot self = this;
				Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
					public void run() {
						self.plugin.reloaders.remove(player.getName());
					}
				}, 40L);
				
			} else if (hand.getType() == Material.DIAMOND_HOE) { // Mini-gun
				if (ammo != null && ammo.getAmount() >= 1) {
					if (ammo.getAmount() == 1)
						player.getInventory().remove(ammo);
					else
						ammo.setAmount(ammo.getAmount() - 1);
					player.updateInventory();
				} else {
					plugin.reload(player);
					return;
				}

				player.launchProjectile(Snowball.class);
				player.launchProjectile(Snowball.class);
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
				smokepase(player, loc);
				player.playSound(player.getLocation(), Sound.CLICK, 160.0F, 0.0F);
			} else if (hand.getType() == Material.WOOD_HOE) { // Pistol
		        if (this.plugin.pistol.contains(player.getName())) {
		        	return;
		        }
		        
				if (ammo != null && ammo.getAmount() >= 1) {
					if (ammo.getAmount() == 1)
						player.getInventory().remove(ammo);
					else
						ammo.setAmount(ammo.getAmount() - 1);
					player.updateInventory();
				} else {
					plugin.reload(player);
					return;
				}

				this.plugin.pistol.add(player.getName());
				player.launchProjectile(Snowball.class);
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
				smokepase(player, loc);
				player.playSound(player.getLocation(), Sound.CLICK, 160.0F, 0.0F);
				
				final Shoot self = this;
				Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
					public void run() {
						self.plugin.pistol.remove(player.getName());
					}
				}, 12L);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamageArrow(EntityDamageByEntityEvent ev)
  {
    Entity damager = ev.getDamager();
    if ((damager instanceof Snowball)) {
      damager = ((Snowball)damager).getShooter();
      ((HumanEntity)damager).getItemInHand();
      ev.setDamage(12);
      }
  }
  @EventHandler
  public void shootArrow(ProjectileLaunchEvent e)
  {
    if ((e.getEntity() instanceof Snowball)) {
      double x = 3.0D;
      Vector v = e.getEntity().getVelocity();
      v.multiply(new Vector(x, x, x));
      e.getEntity().setVelocity(v);
    }
    else if ((e.getEntity() instanceof Arrow)) {
      double x = 1.0D;
      Vector v = e.getEntity().getVelocity();
      v.multiply(new Vector(x, x, x));
      e.getEntity().setVelocity(v);
    }
  }

  @EventHandler
  public void onEntityExplode(EntityExplodeEvent event) {
    event.setCancelled(true);
    Location loc = event.getLocation();
    event.getLocation().getWorld().createExplosion(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 4.0F, false, false);
  }

  public void smokepase(Player player, Location loc) {
    double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
    if (rotation < 0.0D) {
      rotation += 360.0D;
    }
    if ((0.0D <= rotation) && (rotation < 22.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 3);
    }
    else if ((22.5D <= rotation) && (rotation < 67.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 9);
    }
    else if ((67.5D <= rotation) && (rotation < 112.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 1);
    }
    else if ((112.5D <= rotation) && (rotation < 157.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 2);
    }
    else if ((157.5D <= rotation) && (rotation < 202.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 5);
    }
    else if ((202.5D <= rotation) && (rotation < 247.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 8);
    }
    else if ((247.5D <= rotation) && (rotation < 292.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 7);
    }
    else if ((292.5D <= rotation) && (rotation < 337.5D)) {
      player.getWorld().playEffect(loc, Effect.SMOKE, 6);
    }
    else if ((337.5D <= rotation) && (rotation < 360.0D))
      player.getWorld().playEffect(loc, Effect.SMOKE, 3);
  }
}