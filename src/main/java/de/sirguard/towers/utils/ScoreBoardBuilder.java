package de.sirguard.towers.utils;

import de.sirguard.towers.objects.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class ScoreBoardBuilder {

    protected final Scoreboard scoreboard;
    protected final Objective objective;
    protected final Player player;
    protected final SPlayer sPlayer;

    public ScoreBoardBuilder(Player player, String displayName) {
        this.player = player;
        this.sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());
        if (player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        this.scoreboard = player.getScoreboard();
        if (this.scoreboard.getObjective("display") != null)
            this.scoreboard.getObjective("display").unregister();
        this.objective = this.scoreboard.registerNewObjective("display", "dummy", displayName);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        createScoreboard();
    }

    public abstract void update();

    public abstract void createScoreboard();

    public void setDisplayName(String displayName) {
        this.objective.setDisplayName(displayName);
    }

    public void setScore(String content, int score) {
        if(score < 0) {
            System.err.println("Scoreboard Score can't be smaller than 0!");
            return;
        }

        Team team = getTeamByScore(score);
        if (team == null)
            return;
        team.setPrefix(content);
        showScore(score);
    }

    public void removeScore(int score) {
        hideScore(score);
    }

    private EntryName getEntryNameByScore(int score) {
        for (EntryName name : EntryName.values()) {
            if (score == name.getEntry())
                return name;
        }
        return null;
    }

    private Team getTeamByScore(int score) {
        EntryName name = getEntryNameByScore(score);
        if (name == null)
            return null;
        Team team = this.scoreboard.getEntryTeam(name.getEntryName());
        if (team != null)
            return team;
        team = this.scoreboard.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());
        return team;
    }

    private void showScore(int score) {
        EntryName name = getEntryNameByScore(score);
        if (name == null)
            return;
        if (this.objective.getScore(name.getEntryName()).isScoreSet())
            return;
        this.objective.getScore(name.getEntryName()).setScore(score);
    }

    private void hideScore(int score) {
        EntryName name = getEntryNameByScore(score);
        if (name == null)
            return;
        if (!this.objective.getScore(name.getEntryName()).isScoreSet())
            return;
        this.scoreboard.resetScores(name.getEntryName());
    }
}
