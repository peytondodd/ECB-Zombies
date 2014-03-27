package net.endercraftbuild.cod.zombies.listeners;

import java.util.Iterator;
import java.util.List;

import me.confuser.barapi.BarAPI;
import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.GameTickEvent;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.events.PlayerLeaveEvent;
import net.endercraftbuild.cod.utils.FireworkEffectPlayer;
import net.endercraftbuild.cod.zombies.ZombieGame;
import net.endercraftbuild.cod.zombies.events.GameEntityDeathEvent;
import net.endercraftbuild.cod.zombies.events.RoundAdvanceEvent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MiscListener implements Listener {
	
	public CoDMain plugin;
	
	public MiscListener(CoDMain plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler
	public void onTick(GameTickEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		
		
		Iterator<Player> iterator = game.getPlayers().iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			BarAPI.setMessage(player, ChatColor.BLUE  + game.getName() + " Stats: " + ChatColor.DARK_AQUA + "Round: " +  game.getCurrentWave() + ChatColor.AQUA + " |" 
					+ ChatColor.DARK_AQUA + " Zombies Alive: " + game.getRemainingEntityCount() + "/" + game.getMaxEntityCount(), 100);
		
		}
	}
	
	@EventHandler
	public void FireworkRoundAdvance(RoundAdvanceEvent event) {
		final ZombieGame game = (ZombieGame) event.getGame();
	
	FireworkEffectPlayer fireworks = new FireworkEffectPlayer();
	
	final List<Location> blocks = fireworks.circle(game.getSpawnLocation(), 10, 1, true, false, 9);
	new BukkitRunnable() {
	    int maxIterations = 2;
	    int iterations = 0;
	 
	    @Override
	    public void run() {
	        iterations = 0;
	        while (!blocks.isEmpty() && iterations++ < maxIterations) {
	            final Firework fw = (Firework) game.getSpawnLocation().getWorld().spawnEntity(blocks.get(0), EntityType.FIREWORK);
	            FireworkMeta meta = fw.getFireworkMeta();
	 
	            meta.addEffect(FireworkEffect.builder().withColor(Color.BLUE).withColor(Color.WHITE).withFade(Color.WHITE).build());
	 
	            fw.setFireworkMeta(meta);
	 
	            fw.detonate();
	            blocks.remove(0);
	        }
	        if (blocks.isEmpty()) {
	            this.cancel();
	        }
	    }
	}.runTaskTimer(plugin, 0, 1);
	}
		
	
	  @EventHandler
      public void onEntityChangeBlock(EntityChangeBlockEvent event) {
              if (event.getEntityType() == EntityType.FALLING_BLOCK) {
                      
                               event.setCancelled(true);
                      }
              }
      
	 
	@EventHandler(priority=EventPriority.HIGHEST)
	  public void onExplosion(EntityExplodeEvent event)
	  {
	    if (!event.isCancelled()) {
	    	
	    	
	    	///List<Block> blockListclone;
	    	//final List<Block> blockListclone = new ArrayList<Block>(event.blockList());
	    	
	      for (Block block : event.blockList())
	      {
	    	  
	        Vector newVelo = event.getLocation().subtract(event.getLocation()).toVector();
	        
	        newVelo.setX(event.getYield() * 2.0F - newVelo.getX());
	        
	        newVelo.setY(event.getYield() * 2.5F - newVelo.getY());
	        
	        newVelo.setZ(event.getYield() * 2.0F - newVelo.getZ());
	        
	        final FallingBlock fallBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
	        
	        fallBlock.setVelocity(newVelo);
	        
	        fallBlock.setDropItem(false);
	        break;
	      }    
	      event.blockList().clear();
	    }
	  }

	
	
  	//@EventHandler
	public void onGameEntityDeath(GameEntityDeathEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		
	
		Iterator<Player> iterator = game.getPlayers().iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			
		//FakeDragon.setStatus(player, "", 100);
			BarAPI.setMessage(player, ChatColor.BLUE  + game.getName() + " Stats: " + ChatColor.DARK_AQUA + "Round: " +  game.getCurrentWave() + ChatColor.AQUA + " |" 
							+ ChatColor.DARK_AQUA + " Zombies Alive: " + game.getRemainingEntityCount() + "/" + game.getMaxEntityCount(), 100);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ZombieGame game = (ZombieGame) event.getGame();
		Player player = event.getPlayer();
		BarAPI.setMessage(player, ChatColor.BLUE  + game.getName() + " Stats: " + ChatColor.DARK_AQUA + "Round: " +  game.getCurrentWave() + ChatColor.AQUA + " |" 
				+ ChatColor.DARK_AQUA + " Zombies Alive: " + game.getLivingEntityCount() + "/" + game.getMaxEntityCount(), 100);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerLeaveEvent event) {
		Player player = event.getPlayer();
		
		player.teleport(player.getWorld().getSpawnLocation());
		BarAPI.setMessage(player, ChatColor.DARK_AQUA + "Visit " + ChatColor.AQUA + "ecb-mc.net " + ChatColor.DARK_AQUA + "| Donate | Forums | Vote", 100);
		
	}
	
	
	
}
