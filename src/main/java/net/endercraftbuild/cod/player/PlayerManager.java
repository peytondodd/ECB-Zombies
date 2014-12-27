package net.endercraftbuild.cod.player;

import net.endercraftbuild.cod.CoDMain;
import net.endercraftbuild.cod.events.PlayerLeaveGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.*;

/**
 * Created by CP on 12/1/2014.
 */
public class PlayerManager implements Listener {


    private final CoDMain plugin;
    private final HashMap<UUID, CoDPlayer> players;



    public PlayerManager(CoDMain plugin) {
        this.plugin = plugin;
        players = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }


    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

            @Override
            public void run() {
                savePlayer(event.getPlayer().getUniqueId());
            }
        });
    }




    @EventHandler
    public void join(final PlayerJoinEvent event) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

            @Override
            public void run() {
                loadPlayer(event.getPlayer().getUniqueId());
            }
        });
    }


    public void savePlayer(UUID u) {
        Long started = System.currentTimeMillis();
        CoDPlayer p = getPlayer(u);

        Jedis jedis = plugin.getJedisPool().getResource();

            Map<String, String> m = new HashMap<>();
            m.put("exp", Double.toString(p.getXp()));
            m.put("level", Integer.toString(p.getLevel()));
            m.put("revives", Integer.toString(p.getRevives()));
            m.put("doorsOpened", Integer.toString(p.getDoorsOpened()));
            m.put("weaponsBought", Integer.toString(p.getWeaponsBought()));
            m.put("rounds", Integer.toString(p.getRoundsSurvived()));
            jedis.hmset(u.toString(), m);


        players.remove(u, p);
        plugin.getJedisPool().returnResource(jedis);
        System.out.println(System.currentTimeMillis() - started + "ms save time");

    }



    public void loadPlayer(UUID u) {
        Long started = System.currentTimeMillis();
        CoDPlayer p = new CoDPlayer(Bukkit.getPlayer(u), plugin);
        Jedis jedis = plugin.getJedisPool().getResource();

        //yay
        if(!jedis.exists(u.toString())) {
            System.out.println("test");
            Map<String, String> m = new HashMap<>();
            m.put("exp", "0");
            m.put("level", "0");
            m.put("revives", "0");
            m.put("doorsOpened", "0");
            m.put("weaponsBought", "0");
            m.put("rounds", "0");
            jedis.hmset(u.toString(), m);
            //Global stats - lets see how many players we have!!
            jedis.hincrBy("global", "totalPlayers", 1);

        }

                                                    //   0        1          2          3              4              5
        List<String> loaded = jedis.hmget(u.toString(), "exp", "level", "revives", "doorsOpened", "weaponsBought", "rounds");
            p.setXp(Double.parseDouble(loaded.get(0)));
            p.setLevel(Integer.parseInt(loaded.get(1)));
            p.setRevives(Integer.parseInt(loaded.get(2)));
            p.setDoorsOpened(Integer.parseInt(loaded.get(3)));
            p.setWeaponsBought(Integer.parseInt(loaded.get(4)));
            p.setRoundsSurvived(Integer.parseInt(loaded.get(5)));



        players.put(u, p);
        plugin.getJedisPool().returnResource(jedis);
        System.out.println(System.currentTimeMillis() - started + "ms load time");
       // printDebug(p);
    }

    public void giveExp(List<Player> players) {
        Jedis jedis = plugin.getJedisPool().getResource();
        Pipeline pipeline = jedis.pipelined();


    }

    public void printDebug(CoDPlayer p) {
        System.out.println("=======DEBUG=======" + "\nPlayer: " + p.getBukkitPlayer().getName() + "(" + p.getBukkitPlayer().getUniqueId().toString()
                + ")" + "\nEXP: " + p.getXp() +
                "\nLevel: " + p.getLevel() + "\nRevives: " + p.getRevives() + "\nDoors Opened: " + p.getDoorsOpened() + "\nWeapons Bought: " + p.getWeaponsBought());

    }

    public void giveXp(CoDPlayer p, int exp) {
        Player player = p.getBukkitPlayer();

        //fukkit no dynamic
        if (player.hasPermission("cod.donor.1")) {
            p.addXp(exp * 1.5);
            player.sendMessage(ChatColor.GOLD + "+" + (exp * 1.5) + " EXP! (x1.5 Slayer)");
        } else if (player.hasPermission("cod.donor.2")) {
            p.addXp(exp * 2);
            player.sendMessage(ChatColor.GOLD + "+" + exp * 2 + " EXP! (x2 Slayer+)");
        } else if (player.hasPermission("cod.donor.3")) {
            p.addXp(exp * 2.5);
            player.sendMessage(ChatColor.GOLD + "+" + (exp * 2.5) + " EXP! (x2.5 RANKNAMEHERE)");
        } else {
            p.addXp(exp);
            player.sendMessage(ChatColor.GOLD + "+" + exp + " EXP!");
        }
    }


    public HashMap getPlayers() {
        return players;
    }


    public CoDPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }
    public CoDPlayer getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

}



