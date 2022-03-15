package de.sirguard.towers.listener;

import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(GameManager.playersInBuildMode.contains(event.getPlayer()) || GameManager.gameState != GameState.RUNNING)
            return;

        event.setCancelled(true);
    }
}
