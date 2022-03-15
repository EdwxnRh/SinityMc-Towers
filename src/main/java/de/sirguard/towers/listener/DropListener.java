package de.sirguard.towers.listener;

import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (GameManager.gameState == GameState.RUNNING) return;
        event.setCancelled(true);
    }
}
