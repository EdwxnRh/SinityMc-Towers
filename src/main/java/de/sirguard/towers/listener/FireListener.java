package de.sirguard.towers.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FireListener implements Listener {

    @EventHandler
    public void onFire(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!(event.getFrom().getBlock().getType() == Material.FIRE)) return;
            player.setFireTicks(30);

    }

}
