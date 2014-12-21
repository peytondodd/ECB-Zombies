package net.endercraftbuild.cod.zombies.listeners;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerJoinEvent;
import net.endercraftbuild.cod.zombies.ZombieGame;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by CP on 12/10/2014.
 */
public class AdvanceListener  implements Listener {

    public CoDMain plugin;

    public AdvanceListener (CoDMain plugin) {
        this.plugin = plugin;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ZombieGame game = (ZombieGame) event.getGame();
        if(game.isAdvanced()) {
            Player p = event.getPlayer();
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "WARN: This game is ADVANCED\n- Zombies have more health\n- Zombies run faster\n- There are more zombies\n- You can EXO suit double jump\n- Exo suit perks (SOON)");
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

        if(game == null) {
            return;
        }

        if(!game.isAdvanced()) { //only advance games!
            return;
        }
        if ((event.getPlayer().getGameMode() != GameMode.CREATIVE) && (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) && (event.getPlayer().getGameMode() != GameMode.ADVENTURE)) {
            event.getPlayer().setAllowFlight(true);
        }
    }

    //WG flag in dead player lobby to prevent that
    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

        if(game == null) {
            return;
        }

        if(!game.isAdvanced()) { //only advance games!
            return;
        }
        if ((player.getGameMode() != GameMode.CREATIVE)) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(0.2D).setY(1.1D));
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0F, -5.0F);

        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) {
            Player p = (Player)event.getEntity();
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                ZombieGame game = (ZombieGame)plugin.getGameManager().get(p);


                if(game == null)
                    return;

                if(!game.isAdvanced())
                    return;

                event.setCancelled(true);
            }


        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(plugin.getGameManager().get(player) == null)
            return;

        ZombieGame game = (ZombieGame) plugin.getGameManager().get(player);

        if(!game.isAdvanced())
            return;


        if(event.getItem() != null && event.getItem().getType() == Material.PAPER) {
            if(event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName() != null) {
                switch(event.getItem().getItemMeta().getDisplayName()) {
                    //speed
                    case "Exo Overclock" :
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 5));
                        ItemStack is = player.getItemInHand();
                        if(is != null) {
                            if(is.getAmount() > 1)
                                is.setAmount(is.getAmount() - 1);
                            else player.setItemInHand(null);
                        }
                        player.sendMessage(plugin.prefix + "Exo Overclock active! 6 second super speed!");
                    //health
                    case "Exo Stim" :

                    case "":

                }
            }
        }
    }
}
