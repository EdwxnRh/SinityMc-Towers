package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.objects.*;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class SetupListeners implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!GameManager.playersInTeamsEditMode.containsKey(event.getPlayer())) return;
        Team team = (Team) GameManager.playersInTeamsEditMode.get(event.getPlayer()).get("team");
        Player p = event.getPlayer();

        if(event.getHand() != EquipmentSlot.HAND) {
            event.setCancelled(true);
            return;
        }

        switch (p.getItemInHand().getType()) {
            case PLAYER_HEAD -> {
                team.setSpawn(p.getLocation());
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Spawn von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName() + " §7gesetzt.");
            }
            case GOLD_INGOT -> {
                team.setBanker(new Banker(p.getLocation()));
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Bankier von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName() + " §7gesetzt.");
            }
            case AMETHYST_SHARD -> {
                team.setUpgrades(new Upgrades(p.getLocation()));
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Upgrades-Villager von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName() + " §7gesetzt.");
            }
            case EMERALD -> {
                team.setMerchant(new Merchant(p.getLocation()));
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Merchant von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName() + " §7gesetzt.");
            }
            case IRON_AXE -> {
                if(team.getTower() == null)
                    team.setTower(new Tower());

                if(event.getAction().isRightClick()) {
                    team.getTower().setLocation1(Objects.requireNonNull(event.getClickedBlock()).getLocation());
                    p.sendMessage(Towers.prefix + "Du hast erfolgreich die Tower Pos 1 von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName()+ " §7gesetzt.");
                } else if(event.getAction().isLeftClick()) {
                    team.getTower().setLocation2(Objects.requireNonNull(event.getClickedBlock()).getLocation());
                    p.sendMessage(Towers.prefix + "Du hast erfolgreich die Tower Pos 2 von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName()+ " §7gesetzt.");
                }
            }
            case CRIMSON_FUNGUS -> {
                team.setOlli(new Olli(p.getLocation()));
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Olli von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName()+ " §7gesetzt.");
            }
            case WARPED_FUNGUS -> {
                if(team.getTower() == null)
                    team.setTower(new Tower());

                team.getTower().setShroomieLocation(p.getLocation());
                p.sendMessage(Towers.prefix + "Du hast erfolgreich den Bauarbeiter von Team §" + Teams.getColorOfTeam(team.getName()) + team.getName()+ " §7gesetzt.");
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onSetupDrop(PlayerDropItemEvent event) {
        if(!GameManager.playersInTeamsEditMode.containsKey(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onSetupItemDrag(InventoryClickEvent event) {
        if(!GameManager.playersInTeamsEditMode.containsKey((Player) event.getWhoClicked())) return;
        event.setCancelled(true);
    }
}