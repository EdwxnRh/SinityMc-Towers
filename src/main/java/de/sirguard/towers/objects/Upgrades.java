package de.sirguard.towers.objects;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Objects;

public class Upgrades {
    private Location location;
    public Villager upgrades;

    public Upgrades(Location location) {
        this.location = location;
    }

    public void spawn() {
        upgrades = (Villager) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.VILLAGER);
        upgrades.setSilent(true);
        upgrades.setInvulnerable(true);
        upgrades.setCustomName("§aUpgrades");
        upgrades.setAI(false);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
