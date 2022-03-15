package de.sirguard.towers.listener;


import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.utils.GameManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;

public class LoginListener implements Listener {

    public static ArrayList<String> relogable_players = new ArrayList<>();

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (GameManager.gameState == GameState.RUNNING) {
            if (relogable_players.contains(player.getUniqueId().toString())) {
                event.allow();
            } else
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cDas Spiel ist bereits gestartet!"));
        }
    }
}
