package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.objects.Team;
import de.sirguard.towers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.regex.Pattern;

public class TeamMapSelectListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (GameManager.gameState == GameState.RUNNING || GameManager.playersInBuildMode.contains(event.getWhoClicked())) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() != null) {
            String title = event.getView().getTitle();

            if (title.equals("§8Teamauswahl")) {
                int slot = event.getRawSlot();
                if (slot >= 0 && event.getCurrentItem() != null) {
                    Team team = Team.getTeamBySlot(slot);

                    assert team != null;
                    if(Team.getTeamByPlayer(player) != null && Objects.equals(Team.getTeamByPlayer(player), team.getName())){
                        team.removePlayer(player);
                        return;
                    }

                    if (team.getPlayers().size() == GameManager.teamSize) {
                        player.sendMessage(Towers.error + "§7Das Team ist bereits voll!");
                        return;
                    }

                    team.addPlayer(player);
                    GameManager.playersMap.get(player.getUniqueId().toString()).setTeam(team);

                    SPlayer p = GameManager.playersMap.get(player.getUniqueId().toString());
                    p.getScoreBoard().setScore("§a§lTeam: §f" + p.getTeam().getName(), 1);

                    player.sendMessage(Towers.prefix + "§7Du bist nun in §" + Teams.getTeamByDisplayName(team.getName()).getColor() + "Team " + team.getName());
                }
            } else if (title.equals("§8Map Voting")) {
                if (event.getCurrentItem() == null) return;
                ItemStack item = event.getCurrentItem();
                String map = item.getItemMeta().getDisplayName().replaceAll(Pattern.compile("§7 by §6\\w+").pattern(), "").replaceAll("§6", "");
                GameManager.votedMap.put(player, GameManager.maps.get(map));


                ItemBuilder itemBuilder = new ItemBuilder(Material.GLOBE_BANNER_PATTERN).setDisplayName(item.getItemMeta().getDisplayName()).setItemFlag(ItemFlag.HIDE_POTION_EFFECTS);
                itemBuilder.setLore("§7Ausgewählt von §6" + (GameManager.countVotes(map) == 1 ? "einem §7Spieler" : GameManager.countVotes(map) + " §6Spielern"));
                PreGameSelectListener.voting.setItem(event.getSlot(), itemBuilder.build());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getOpenInventory().getTopInventory() == PreGameSelectListener.voting) {
                        PreGameSelectListener.voting.setItem(event.getSlot(), itemBuilder.build());
                    }
                }

                player.sendMessage(Towers.prefix + "§7Du hast für die Map " + item.getItemMeta().getDisplayName() + " §7gevoted.");
            }
        }
    }
}
