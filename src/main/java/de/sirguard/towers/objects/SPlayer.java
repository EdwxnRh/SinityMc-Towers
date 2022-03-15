package de.sirguard.towers.objects;

import de.sirguard.towers.utils.ScoreBoard;

public class SPlayer {

    private Team team;
    private int balance = 0;
    private ScoreBoard scoreBoard;
    private boolean inCombat = false;

    public String getKit() {
        return "none";
    }

    public void addBalance() {
        addBalance(1);
    }

    public void addBalance(int balance) {
        this.balance += balance;
    }

    public Team getTeam() {
        return team;
    }

    public int getBalance() {
        return balance;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
}
