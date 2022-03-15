package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.utils.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(GameManager.playersInBuildMode.contains(event.getPlayer()))
            return;

        Location fixedBlockLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX() + 0.5, event.getBlock().getY(), event.getBlock().getZ() + 0.5, 0, 0);

        if (GameManager.spawnerLocations.contains(fixedBlockLocation)) {
            event.getBlock().getWorld().dropItemNaturally(fixedBlockLocation.add(0, 1, 0), new ItemStack(Towers.ziegel));
        }

        if(event.getBlock().getType() == Towers.towerBlock) {
            event.setDropItems(false);
            return;
        }


        event.setCancelled(true);
    }

    @EventHandler
    public void onTntBreak(EntityExplodeEvent event) {
        ArrayList<Block> blocks = new ArrayList<>();

        for (Block block : event.blockList()) {
            if(block.getType() == Towers.towerBlock) {
                blocks.add(block);
            }
        }
        event.blockList().clear();
        event.blockList().addAll(blocks);
        event.setYield(0);
    }
}
