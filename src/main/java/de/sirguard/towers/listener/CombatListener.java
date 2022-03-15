package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.objects.Team;
import de.sirguard.towers.utils.GameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;


import java.util.ArrayList;
import java.util.UUID;

public class CombatListener implements Listener {

    private final ArrayList<UUID> gotHit = new ArrayList<>();
    private  BukkitTask taskId;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        resetCooldown(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        resetCooldown(event.getPlayer());
        Player player = event.getPlayer();
        SPlayer p = GameManager.playersMap.get(player.getUniqueId().toString());
        event.setRespawnLocation(p.getTeam().getSpawn());
        for (ItemStack item : p.getTeam().getItems()) {
            player.getInventory().addItem(item);
        }
        p.getTeam().setArmor(player);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        SPlayer player = GameManager.playersMap.get(p.getUniqueId().toString());
        player.setInCombat(true);
        if (gotHit.contains(p.getUniqueId())) {
           if (taskId != null) {
               Bukkit.getScheduler().cancelTask(taskId.getTaskId());
           }
        } else {
            gotHit.add(p.getUniqueId());
        }

         taskId = Bukkit.getScheduler().runTaskLater(Towers.plugin, new Runnable() {
            @Override
            public void run() {
                gotHit.remove(p.getUniqueId());
                player.setInCombat(false);
            }
        },20*5);

        if (event.getDamage() >= p.getHealth()) {
            //DEATHEVENT
            onDeath(p, damager);
            event.setCancelled(true);
        }
    }




    public void resetCooldown(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100D);
    }


    public void onDeath(Player entity, Player killer) {
        Location deathLocation = entity.getLocation();
        for (int i = 1; i < 30; i++) {
            deathLocation.getWorld().spawnParticle(Particle.CLOUD, deathLocation, i);
        }

        entity.sendMessage(Team.getTeamByPlayer(entity).getName());
        entity.sendMessage("hallo");

        if (killer != null) {
            Bukkit.broadcast(Component.text(Towers.prefix + "§" + Teams.getTeamByDisplayName(Team.getTeamByPlayer(entity)).getColor() + entity.getName() + " §7wurde von §" + Teams.getTeamByDisplayName(Team.getTeamByPlayer(killer)).getColor() + killer.getName() + " §7getötet!"));
        } else {
            Bukkit.broadcast(Component.text(Towers.prefix + "§" + Teams.getTeamByDisplayName(Team.getTeamByPlayer(entity)).getColor() + entity.getName() + " §7ist gestorben."));
        }
        entity.getInventory().clear();

        SPlayer player = GameManager.playersMap.get(entity.getUniqueId().toString());
        int balance = player.getBalance() /2;
        player.addBalance(-balance);
        for (int i = 0; i < balance; i++) {
            deathLocation.getWorld().dropItemNaturally(deathLocation, new ItemStack(Towers.ziegel, 1));
        }

        entity.setHealth(entity.getMaxHealth());

        onRespawn(entity);
    }

    public void onRespawn(Player player) {
        resetCooldown(player);
        SPlayer p = GameManager.playersMap.get(player.getUniqueId().toString());
        player.teleport(p.getTeam().getSpawn());
        for (ItemStack item : p.getTeam().getItems()) {
            player.getInventory().addItem(item);
        }
        p.getTeam().setArmor(player);
    }


}
