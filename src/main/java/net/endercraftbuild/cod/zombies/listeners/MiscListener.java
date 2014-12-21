package net.endercraftbuild.cod.zombies.listeners;

import java.util.Iterator;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.utils.Utils;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.GameEntityDeathEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import net.endercraftbuild.cod.zombies.objects.GameEntity;
import net.endercraftbuild.cod.zombies.tasks.FireworkTask;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MiscListener implements Listener {
	
	public CoDMain plugin;
	FireworkTask fireworktask;
	
	public MiscListener(CoDMain plugin) {
		this.plugin = plugin;
		
	}

	
	@EventHandler
	public void onTick(GameTickEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();



		Iterator<Player> iterator = game.getPlayers().iterator();
		while (iterator.hasNext()) {

			Player player = iterator.next();
			game.createNewScoreboard(player);

		}
	}
	
	@EventHandler
	public void FireworkRoundAdvance(RoundAdvanceEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		
		new FireworkTask(game.getSpawnLocation().getWorld(), Utils.circle(game.getSpawnLocation(), 10, 1, true, false, 9)).runTask(plugin);
	}
		
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.FALLING_BLOCK)
			event.setCancelled(true);
	}

    @EventHandler
    public void onLastZombie(GameEntityDeathEvent event) {
        ZombieGame game = (ZombieGame)event.getGame();

        if(game.getLivingEntityCount() == 2) {

            for(Player p : game.getPlayers()) {
                if(!p.getInventory().contains(Material.COMPASS)) {
                    ItemStack c = new ItemStack(Material.COMPASS);
                    Utils.setItemName(c, ChatColor.RED + "Find Last Zombie");
                    p.getInventory().addItem(c);
                    //should always return last zombie
                    p.setCompassTarget(game.getGameEntities().get(0).getMob().getLocation());
                    p.sendMessage(plugin.prefix + "One zombie left! Check your compass for help finding it!");
                }

            }

        }

    }

    @EventHandler
    public void onFire(BlockIgniteEvent event) {
        if(event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAdvance(RoundAdvanceEvent event) {
        ZombieGame game = (ZombieGame)event.getGame();
        for (Player player : game.getPlayers()) {
            if(player.getInventory().contains(Material.COMPASS)) {
                player.getInventory().remove(Material.COMPASS);
            }
        }

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent event) {

        event.blockList().clear();
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.getEntityType() == EntityType.ZOMBIE) {
            if(event.getDamager() instanceof Player) {

                Player damager = (Player)event.getDamager();


                if(plugin.getGameManager().get(damager) == null)
                    return;

                ZombieGame game = (ZombieGame)plugin.getGameManager().get(damager);

                if(game.isInstaKill()) {
                    event.setDamage(9999); //still want to count towards round
                }
                //not the best but it works...
            } else if(event.getDamager() instanceof Projectile) {
                Player d = (Player)((Projectile) event.getDamager()).getShooter();

                if(plugin.getGameManager().get(d) == null)
                    return;

                ZombieGame game = (ZombieGame)plugin.getGameManager().get(d);

                if(game.isInstaKill()) {
                    event.setDamage(9999);
                }
            }

        }

    }

	
}
