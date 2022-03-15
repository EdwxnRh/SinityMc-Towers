package de.sirguard.towers.objects;

import org.bukkit.Location;

public class Map {
    private final String name;
    private final String builder;
    private final int teamAmount;
    private final Location spectatorLocation;

    public Map(String name, String builder, int teamAmount, Location spectatorLocation) {
        this.name = name;
        this.builder = builder;
        this.teamAmount = teamAmount;
        this.spectatorLocation = spectatorLocation;
    }

    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public Location getSpectatorLocation() {
        return spectatorLocation;
    }
}
