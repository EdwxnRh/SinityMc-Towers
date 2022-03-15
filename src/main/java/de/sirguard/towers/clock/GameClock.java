package de.sirguard.towers.clock;

import de.sirguard.towers.Towers;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.objects.Team;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.Bukkit;

public class GameClock {

    private int runningTime = 0;

    public void start() {
        Bukkit.getScheduler().runTaskTimer(Towers.plugin, new Runnable() {
            @Override
            public void run() {
                runningTime++;
                for (SPlayer sPlayer : GameManager.playersMap.values()) {
                    if(sPlayer.getScoreBoard() != null)
                        sPlayer.getScoreBoard().update();
                }

                for (Team team : GameManager.teams) {
                    team.getTower().build();
                }
            }
        },0,20);
    }

    public int getRunningTime() {
        return runningTime;
    }
}
