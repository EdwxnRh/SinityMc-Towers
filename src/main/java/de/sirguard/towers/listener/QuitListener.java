package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GameManager.playersInTeamsEditMode.remove(player);

        GameManager.playersInBuildMode.remove(player);

        GameManager.votedMap.remove(player);

        if (GameManager.gameState == GameState.RUNNING) {
            LoginListener.relogable_players.add(player.getUniqueId().toString());

            Bukkit.getScheduler().scheduleSyncDelayedTask(Towers.plugin, new Runnable() {
                @Override
                public void run() {
                    if (LoginListener.relogable_players.contains(player.getUniqueId().toString())) {
                        LoginListener.relogable_players.remove(player.getUniqueId().toString());
                        if (GameManager.playersMap.containsKey(player.getUniqueId().toString())) {
                            GameManager.playersMap.remove(player.getUniqueId().toString()).getTeam().removePlayer(player);
                        }
                    }

                }
            }, 20*30);
        } else {
            if (GameManager.playersMap.containsKey(player.getUniqueId().toString())) {
                GameManager.playersMap.remove(player.getUniqueId().toString()).getTeam().removePlayer(player);
            }
        }
    }
}
