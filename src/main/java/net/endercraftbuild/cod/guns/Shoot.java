package net.endercraftbuild.cod.guns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.player.CoDPlayer;
import net.endercraftbuild.cod.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class Shoot implements Listener {

	private final CoDMain plugin;

	public Shoot(CoDMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        final Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();

        if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
            if (hand.getType() == Material.IRON_HOE) { // AK-47
                if (!consumeAmmo(player, 2)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }

                player.launchProjectile(Snowball.class);
                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.FIREWORK_BLAST2, 70.0F, 0.0F);

            } else if (hand.getType() == Material.STONE_HOE) { // Shotgun
                if (this.plugin.reloaders.contains(player.getName()))
                    return;

                if (!consumeAmmo(player, 3)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }

                this.plugin.reloaders.add(player.getName());
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
                }, 22L);

            } else if (hand.getType() == Material.STONE_PICKAXE) { // MP5

                if (!consumeAmmo(player, 1)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }


                player.launchProjectile(Snowball.class);

                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.CLICK, 160.0F, 0.0F);
            } else if (hand.getType() == Material.GOLD_PICKAXE) { // M490

                if (!consumeAmmo(player, 2)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }


                player.launchProjectile(Snowball.class);
                player.launchProjectile(Snowball.class);

                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 160.0F, 0.0F);

            } else if (hand.getType() == Material.IRON_PICKAXE) { // M4

                if (!consumeAmmo(player, 1)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }


                player.launchProjectile(Snowball.class);

                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 70.0F, 70.0F);

            } else if (hand.getType() == Material.DIAMOND_AXE) { // rpg
                if (this.plugin.reloaders.contains(player.getName()))
                    return;

                if (!consumeAmmo(player, 6)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }

                this.plugin.reloaders.add(player.getName());
                player.launchProjectile(WitherSkull.class);

                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 70.0F, 70.0F);

                final Shoot self = this;
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        self.plugin.reloaders.remove(player.getName());
                    }
                }, 65L);

            } else if (hand.getType() == Material.GOLD_HOE) { // Sniper
                if (this.plugin.reloaders.contains(player.getName()))
                    return;

                if (!consumeAmmo(player, 1)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }

                this.plugin.reloaders.add(player.getName());
                player.launchProjectile(Snowball.class);
                player.launchProjectile(Snowball.class);
                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.PISTON_RETRACT, 70.0F, 70.0F);

                final Shoot self = this;
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        self.plugin.reloaders.remove(player.getName());
                    }
                }, 30L);

            } else if (hand.getType() == Material.DIAMOND_HOE) { // RAY GUN
                if (this.plugin.pistol.contains(player.getName()))
                    return;

                if (!consumeAmmo(player, 1)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
                    return;
                }

                // Pistol fire rate
                this.plugin.pistol.add(player.getName());
                player.launchProjectile(Snowball.class);
                player.launchProjectile(Snowball.class);
                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.FIZZ, 160.0F, 0.0F);

                final Shoot self = this;
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        self.plugin.pistol.remove(player.getName());
                    }
                }, 11L);

            } else if (hand.getType() == Material.BLAZE_ROD) { // wund
                if (this.plugin.reloaders.contains(player.getName()))
                    return;

                if(!player.hasPermission("cod.donor")) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "DONOR ONLY! Donate @ ecb-mc.net");
                    return;
                }

                if (!isCharged(hand.getItemMeta())) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of charge!");
                    return;
                }


                this.plugin.reloaders.add(player.getName());

                Location l = player.getTargetBlock(null, 150).getLocation();

                for (Location cl : Utils.circle(l, 2, 1, false, false, 0)) {
                    player.getWorld().strikeLightning(cl);
                }

                Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(1)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
                smokepase(player, loc);
                player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 70.0F, 70.0F);
                CoDPlayer cp = plugin.getPlayerManager().getPlayer(player);
                cp.payPlayer(player, 25); //flat 25 for wunderwaff w/e

                int loweredcharge = Integer.parseInt(hand.getItemMeta().getLore().get(1)) - 1;
                ArrayList<String> s = new ArrayList<>();
                s.add(ChatColor.YELLOW + "Charges left:"); //0
                s.add(Integer.toString(loweredcharge)); //1
                Utils.setItemLore(hand, s);
                player.updateInventory();
                player.sendMessage(plugin.prefix + "Charge depleting! Use this wisely!");

                final Shoot self = this;
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        self.plugin.reloaders.remove(player.getName());
                    }
                }, 22L);

            } else if (hand.getType() == Material.WOOD_HOE) { // Pistol
                if (this.plugin.pistol.contains(player.getName())) {
                    return;
                }

                if (!consumeAmmo(player, 1)) {
                    player.sendMessage(plugin.prefix + ChatColor.RED + "Out of ammo! Buy some at an ammo sign!");
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
                }, 14L);
            }
        }
    }

    @EventHandler
    public void onLightningHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof LightningStrike))
            return;


        event.setDamage(1000);
    }

	@EventHandler
	public void onBulletHit(EntityDamageByEntityEvent event) { // Bullet hit
		Entity damager = event.getDamager();

		if (!(damager instanceof Snowball))
			return;

		damager = Utils.getDamager(event);
		
		if (!(damager instanceof Player))
			return;
		
		ItemStack item = ((HumanEntity) damager).getItemInHand();
		boolean pap = Utils.isGunPaP(item);
		
		// Sniper bullets deal more damage!
		switch(item.getType()) {
		case GOLD_HOE:
			event.setDamage(pap ? 60 : 45);
			break;
		case STONE_HOE:
			event.setDamage(pap ? 50 : 33);
			break;
		case WOOD_HOE:
		case IRON_HOE:
			event.setDamage(pap ? 36 : 24);
			break;
        case GOLD_PICKAXE:
            event.setDamage(pap ? 37 : 25);
            System.out.println(event.getDamage());
            break;
		case IRON_PICKAXE:
			event.setDamage(pap ? 30 : 20);
			break;
		case STONE_PICKAXE: //SMG
			event.setDamage(pap ? 28 : 18);
            break;
		case DIAMOND_HOE:
			event.setDamage(pap ? 40 : 28);
			break;
		default:
			event.setDamage(0);
			break;
		}
	}
		
	@EventHandler
	public void onRocketHit(ProjectileHitEvent event) { // Make rocket explode
		if (event.getEntity().getType() != EntityType.WITHER_SKULL)
			return;
		
		Player player = (Player) event.getEntity().getShooter();

		if (player.getItemInHand().getType() == Material.DIAMOND_AXE) 
			player.getWorld().createExplosion(event.getEntity().getLocation(), 2.0F, false);
	}
	
	@EventHandler
	public void onRayGunHit(ProjectileHitEvent event) { // Make ray gun explode
		if (event.getEntity().getType() != EntityType.SNOWBALL)
			return;

		if (!(event.getEntity().getShooter() instanceof Player))
			return;

		Player player = (Player) event.getEntity().getShooter();

		if (player.getItemInHand().getType() == Material.DIAMOND_HOE) 
			player.getWorld().createExplosion(event.getEntity().getLocation(), 1.8F, false);
	}

	@EventHandler
	public void shootArrow(ProjectileLaunchEvent event) {
		if ((event.getEntity() instanceof Snowball)) {
			double x = 3.0D;
			Vector v = event.getEntity().getVelocity();
			v.multiply(new Vector(x, x, x));
			event.getEntity().setVelocity(v);
		} else if ((event.getEntity() instanceof Arrow)) {
			double x = 1.0D;
			Vector v = event.getEntity().getVelocity();
			v.multiply(new Vector(x, x, x));
			event.getEntity().setVelocity(v);
		}
	}

	public void smokepase(Player player, Location loc) {
		double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
		if (rotation < 0.0D)
			rotation += 360.0D;
		if ((0.0D <= rotation) && (rotation < 22.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 3);
		else if ((22.5D <= rotation) && (rotation < 67.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 9);
		else if ((67.5D <= rotation) && (rotation < 112.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 1);
		else if ((112.5D <= rotation) && (rotation < 157.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 2);
		else if ((157.5D <= rotation) && (rotation < 202.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 5);
		else if ((202.5D <= rotation) && (rotation < 247.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 8);
		else if ((247.5D <= rotation) && (rotation < 292.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 7);
		else if ((292.5D <= rotation) && (rotation < 337.5D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 6);
		else if ((337.5D <= rotation) && (rotation < 360.0D))
			player.getWorld().playEffect(loc, Effect.SMOKE, 3);
	}

	@SuppressWarnings("deprecation")
	public boolean consumeAmmo(Player player, int count) {
		Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(Material.CLAY_BALL);

		int found = 0;
		for (ItemStack stack : ammo.values())
			found += stack.getAmount();
		if (count > found)
			return false;

		for (Integer index : ammo.keySet()) {
			ItemStack stack = ammo.get(index);

			int removed = Math.min(count, stack.getAmount());
			count -= removed;

			if (stack.getAmount() == removed)
				player.getInventory().setItem(index, null);
			else
				stack.setAmount(stack.getAmount() - removed);

			if (count <= 0)
				break;
		}

		player.updateInventory();
		return true;
	}

    public boolean isCharged(ItemMeta meta) {
        if (meta.hasLore()) {
            if (meta.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Wunderwaffe DG-2")) {
                if (Integer.parseInt(meta.getLore().get(1)) > 0) { //this is awful, *shrug*
                    return true;
                } else {
                    return false;

                }

            }
        }
        return false;
    }

	@EventHandler //Don't allow picks to be enchanted	
	public void pickEnchant(EnchantItemEvent event) {
		if(event.getItem().getType() == Material.STONE_PICKAXE || event.getItem().getType() == Material.IRON_PICKAXE  || event.getItem().getType() == Material.GOLD_PICKAXE|| event.getItem().getType() == Material.DIAMOND_AXE) {
			event.setCancelled(true);
			event.getEnchanter().sendMessage(plugin.prefix + ChatColor.RED + "Guns can be Pack-a-punched at PaP signs!");
					
		}
			
		
	}
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		
		if(!plugin.getGameManager().isInGame(player))
			return;
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.GOLD_HOE) { //SNIPER
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 12));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.WOOD_HOE) { //PISTOL
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 7));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.STONE_PICKAXE) { //SMG
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 6));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.DIAMOND_HOE) { //RAY GUN
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 7));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.STONE_HOE) { //SHOTGUN
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 5));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.IRON_HOE) { //AK47
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 8));	
			
		}
		if(!(player.isSneaking()) && player.getItemInHand().getType() == Material.IRON_PICKAXE) { //M4
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20000, 8));	
			
		}
		else if(player.isSneaking() && player.hasPotionEffect(PotionEffectType.SLOW)) {
			player.removePotionEffect(PotionEffectType.SLOW);
		}
		
	}

}