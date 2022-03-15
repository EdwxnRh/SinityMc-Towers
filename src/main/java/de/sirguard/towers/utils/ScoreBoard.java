package de.sirguard.towers.utils;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.objects.Team;
import org.bukkit.entity.Player;

public class ScoreBoard extends ScoreBoardBuilder {

    public ScoreBoard(Player player) {
        super(player, "  §e§lSinityMC.net  ");
    }
    @Override
    public void createScoreboard() {
        Team team = sPlayer.getTeam();

        if (GameManager.gameState == GameState.VOTING) {
            if (sPlayer.getTeam() != null) {
                setScore("§e§7§m                          ", 3);
                setScore("§b§lKit: §f" + sPlayer.getKit(), 2);
                setScore("§a§lTeam: §f" + sPlayer.getTeam().getName(), 1);
                setScore("§f§7§m                          ", 0);
            }
            setScore("§e§7§m                          ", 3);
            setScore("§b§lKit: §f" + sPlayer.getKit(), 2);
            setScore("§a§lTeam: §f" + "None", 1);
            setScore("§f§7§m                          ", 0);
        } else {

            if (sPlayer.getTeam() == null) return;
            if (sPlayer.isInCombat()) {
                setScore("§e§7§m                          ", 7);
                setScore("§b§lKit: §r§f" + sPlayer.getKit(), 6);
                setScore("§c§lBalance: §f" + sPlayer.getBalance(), 5);
                setScore("§6§lTeam: §f" + sPlayer.getTeam().getBalance(), 4);
                setScore("§a§lProgress: §r§f" + sPlayer.getTeam().getTower().getProgress() + "%", 3);
                setScore("§e§lZeit: §r§f" + shortInteger(Towers.gameClock.getRunningTime()), 2);
                setScore("§c§lIN COMBAT", 1);
            } else {

                setScore("§e§7§m                          ", 6);
                setScore("§b§lKit: §r§f" + sPlayer.getKit(), 5);
                setScore("§c§lBalance: §f" + sPlayer.getBalance(), 4);
                setScore("§6§lTeam: §f" + sPlayer.getTeam().getBalance(), 3);
                setScore("§a§lProgress: §r§f" + sPlayer.getTeam().getTower().getProgress() + "%", 2);
                setScore("§e§lZeit: §r§f" + shortInteger(Towers.gameClock.getRunningTime()), 1);
            }
            setScore("§f§7§m                          ", 0);
        }
    }

    @Override
    public void update() {
        Team team = sPlayer.getTeam();
        if (sPlayer.isInCombat()) {
            setScore("§e§7§m                          ", 7);
            setScore("§b§lKit: §r§f" + sPlayer.getKit(), 6);
            setScore("§c§lBalance: §f" + sPlayer.getBalance(), 5);
            setScore("§6§lTeam: §f" + sPlayer.getTeam().getBalance(), 4);
            setScore("§a§lProgress: §r§f" + sPlayer.getTeam().getTower().getProgress() + "%", 3);
            setScore("§e§lZeit: §r§f" + shortInteger(Towers.gameClock.getRunningTime()), 2);
            setScore("§c§lIN COMBAT", 1);
        } else {
            removeScore(7);
            setScore("§e§7§m                          ", 6);
            setScore("§b§lKit: §r§f" + sPlayer.getKit(), 5);
            setScore("§c§lBalance: §f" + sPlayer.getBalance(), 4);
            setScore("§6§lTeam: §f" + sPlayer.getTeam().getBalance(), 3);
            setScore("§a§lProgress: §r§f" + sPlayer.getTeam().getTower().getProgress() + "%", 2);
            setScore("§e§lZeit: §r§f" + shortInteger(Towers.gameClock.getRunningTime()), 1);
        }
        setScore("§f§7§m                          ", 0);
    }

    public static String shortInteger(int duration) {
        String string = "";

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (duration / 60 / 60 >= 1) {
            hours = duration / 60 / 60;
            duration -= duration / 60 / 60 * 60 * 60;
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration -= duration / 60 * 60;
        }
        if (duration >= 1)
            seconds = duration;

        if (minutes <= 9) {
            string = string + "0" + minutes + ":";
        } else {
            string = string + minutes + ":";
        }
        if (seconds <= 9) {
            string = string + "0" + seconds;
        } else {
            string = string + seconds;
        }
        return string;

    }
}
