package de.sirguard.towers.objects;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Objects;

public class Olli {
    private Location location;
    public Villager olli;

    public Olli(Location location) {
        this.location = location;
    }

    public void spawn() {
        olli = (Villager) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.VILLAGER);
        olli.setSilent(true);
        olli.setInvulnerable(true);
        olli.setCustomName("§aOlli");
        olli.setAI(false);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
