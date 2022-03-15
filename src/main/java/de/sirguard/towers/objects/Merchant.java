package de.sirguard.towers.objects;

import org.bukkit.Location;

public class Merchant {
    // villager that sells useful items for the game like tnt and swords etc
    private Location location;

    public Merchant(Location location) {
        this.location = location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
