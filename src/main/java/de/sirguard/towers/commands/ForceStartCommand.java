package de.sirguard.towers.commands;

import de.sirguard.towers.Towers;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        if(!sender.hasPermission("towers.vip")) {
            player.sendMessage(Towers.error + "Dir fehlen die benötigten Berechtigungen!");
            return false;
        }

        if (GameManager.countdown >= 10) {

            GameManager.forcestarted = true;
            GameManager.loadVotedMap();
            GameManager.countdown = 5;

            player.sendMessage(Towers.prefix + "Du hast das Spiel gestartet!");
        }
        return false;
    }
    // Force start command with permission that allows granted users to start the game, even if lobby isn't full / not enough players but at least 2
}
