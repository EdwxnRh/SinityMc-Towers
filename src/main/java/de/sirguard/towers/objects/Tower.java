package de.sirguard.towers.objects;

import de.sirguard.towers.Towers;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.ArrayList;

public class Tower {
    private Location location1;
    private Location location2;
    private int balance;
    private int speed;
    private float progress;
    private Team team;
    private final ArrayList<Location> blocks = new ArrayList<>();
    private Location lastBlock;
    private Villager villager;
    private Location shroomieLocation;
    private int neededBlocks;
    private int setBlocks;

    private int blockx,blocky,blockz, blockx2,blocky2,blockz2;

    public Tower() {}

    public Tower(Location location1, Location location2, Location shroomieLocation, Team team) {
        this.location1 = location1;
        this.location2 = location2;
        this.shroomieLocation = shroomieLocation;
        this.team = team;

        Location first = this.location1;
        Location sec = this.location2;

        int width;
        int length;
        int height;

        if (first.getBlockX() > sec.getBlockX()) {
            width = first.getBlockX() - sec.getBlockX();
            blockx = sec.getBlockX();
            blockx2 = first.getBlockX();
        } else {
            width = sec.getBlockX() - first.getBlockX();
            blockx = first.getBlockX();
            blockx2 = sec.getBlockX();
        }

        if (first.getBlockZ() > sec.getBlockZ()) {
            length = first.getBlockZ() - sec.getBlockZ();
            blockz = sec.getBlockZ();
            blockz2 = first.getBlockZ();
        } else {
            length = sec.getBlockZ() - first.getBlockZ();
            blockz = first.getBlockZ();
            blockz2 = sec.getBlockZ();
        }

        if (first.getBlockY() > sec.getBlockY()) {
            height = first.getBlockY() - sec.getBlockY();
            blocky = sec.getBlockY();
            blocky2 = first.getBlockY();
        } else {
            height = sec.getBlockY() - first.getBlockY();
            blocky = first.getBlockY();
            blocky2 = sec.getBlockY();
        }

        width += 1;
        length += 1;
        height += 1;


        this.neededBlocks = width*length*height;



        boolean directionZ = false;
        boolean directionX = false;

        for (int y = Math.min(location1.getBlockY(), location2.getBlockY()); y <= Math.max(location1.getBlockY(), location2.getBlockY()); y++) {
            for (int x = Math.min(location1.getBlockX(), location2.getBlockX()); x <= Math.max(location1.getBlockX(), location2.getBlockX()); x++) {
                for (int z = Math.min(location1.getBlockZ(), location2.getBlockZ()); z <= Math.max(location1.getBlockZ(), location2.getBlockZ()); z++) {
                    blocks.add(new Location(location1.getWorld(), directionX ? Math.min(location1.getBlockX(), location2.getBlockX()) + Math.max(location1.getBlockX(), location2.getBlockX()) - x : x, y, directionZ ? Math.min(location1.getBlockZ(), location2.getBlockZ()) + Math.max(location1.getBlockZ(), location2.getBlockZ()) - z : z));
                }
                directionZ = !directionZ;
            }
            directionX = !directionX;
        }
    }

    public void spawn() {
        villager = (Villager) location1.getWorld().spawnEntity(shroomieLocation, EntityType.VILLAGER);
        villager.setBaby();
        villager.setAgeLock(true);
        villager.setAI(false);
        villager.setGravity(false);
        villager.setSilent(true);
        villager.setInvulnerable(true);
    }

    public void build() {
        boolean setted = false;
        setBlocks = 0;
        for (int x = blockx-1; x < blockx2+1; x++) {
            for (int z = blockz-1; z < blockz2+1; z++) {
                for (int y = blocky-1; y < blocky2+1; y++) {
                    if (shroomieLocation.getWorld().getBlockAt(x,y,z).getType() == Towers.towerBlock) {
                        setBlocks++;
                    }
                }
            }
        }



        for (Location block : blocks) {
            lastBlock = block;
            float facing = 0; // south facing
            float pitch = 20; // looking down

            if(balance == 0) {
                villager.teleport(shroomieLocation);
                return;
            }

            if (block.getBlock().getType() != Towers.towerBlock) {
                if (!setted) {
                    block.getWorld().setType(block, Towers.towerBlock);
                    balance--;
                    setted = true;
                } else {
                    if (blocks.indexOf(block) != 0) {
                        Location lastBlockLoc = blocks.get(blocks.indexOf(block) - 1);
                        if (lastBlockLoc.getBlockX() < block.getBlockX()) {
                            facing = -90;
                        }
                        if (lastBlockLoc.getBlockX() > block.getBlockX()) {
                            facing = 90;
                        }
                        if (block.getBlockZ() < lastBlockLoc.getBlockZ()) {
                            facing = 180;
                        }
                        if (block.getBlockY() != lastBlockLoc.getBlockY()) {
                            pitch = 90;
                        }
                        villager.teleport(new Location(lastBlockLoc.getWorld(), lastBlockLoc.getBlockX() + 0.5, lastBlockLoc.getBlockY() + 1, lastBlockLoc.getBlockZ() + 0.5, facing, pitch));
                    }
                    return;
                }
            }
        }



    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    public void addBalance(int balance) {
        this.balance += balance;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setShroomieLocation(Location shroomieLocation) {
        this.shroomieLocation = shroomieLocation;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public int getBalance() {
        return balance;
    }

    public double getProgress() {
        double d =(float)setBlocks/neededBlocks;
        int i = (int) (d*1000);
        double f = (double) i/10;
        return f;
    }

    public Location getShroomieLocation() {
        return shroomieLocation;
    }

    public int getNeededBlocks() {
        return neededBlocks;
    }

    public int getSetBlocks() {
        return setBlocks;
    }
}
