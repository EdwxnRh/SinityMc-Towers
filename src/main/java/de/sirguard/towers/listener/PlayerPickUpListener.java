package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickUpListener implements Listener {
    @EventHandler
    public void onItemPickCollect(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(event.getItem().getItemStack().getType() == Towers.ziegel) {
            event.setCancelled(true);
            GameManager.playersMap.get(player.getUniqueId().toString()).addBalance(event.getItem().getItemStack().getAmount());
            event.getItem().remove();


            for (SPlayer sPlayer : GameManager.playersMap.values()) {
                if(sPlayer.getScoreBoard() != null)
                    sPlayer.getScoreBoard().update();
            }
        }
    }
}
