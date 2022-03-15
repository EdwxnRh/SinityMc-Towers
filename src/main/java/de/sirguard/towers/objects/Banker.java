package de.sirguard.towers.objects;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Objects;

public class Banker {

    private Location location;
    public Villager banker;

    public Banker(Location location) {
        this.location = location;
    }
    public void spawn() {
        banker = (Villager) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.VILLAGER);
        banker.setSilent(true);
        banker.setInvulnerable(true);
        banker.setCustomName("§aBankier");
        banker.setAI(false);
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
