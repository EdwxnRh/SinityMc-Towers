package de.sirguard.towers.listener;

import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.objects.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Team team = GameManager.playersMap.get(event.getPlayer().getUniqueId().toString()).getTeam();

        if(team != null) {
            Bukkit.broadcast(Component.text("§" + Teams.getTeamByDisplayName(team.getName()).getColor() + event.getPlayer().getName() + " §8» §7").append(event.message()));
        } else {
            Bukkit.broadcast(Component.text("§7" + event.getPlayer().getName() + " §8» §7").append(event.message()));
        }
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        event.getPlayer().getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
    }
}
