package de.sirguard.towers.listener;

import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.objects.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class OlliListener implements Listener {
    private final String GUI_NAME = "§aOlli";

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, GUI_NAME);

        inv.setItem(0, new ItemStack(Material.EMERALD_BLOCK));
        inv.setItem(1, new ItemStack(Material.DIAMOND));
        inv.setItem(2, new ItemStack(Material.IRON_BARS));
        inv.setItem(3, new ItemStack(Material.NETHER_BRICK));
        inv.setItem(4, new ItemStack(Material.GOLD_BLOCK));

        player.openInventory(inv);
    }

    @EventHandler
    public void onOlliClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager olli)) return;

        if (Objects.equals(olli.getCustomName(), "§aOlli")) {
            event.setCancelled(true);
            openGUI(event.getPlayer());
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || event.getClickedInventory() == null ||
                !event.getView().getTitle().equals(GUI_NAME) || event.getCurrentItem() == null) return;

        SPlayer sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());

        switch (event.getCurrentItem().getType()) {
            case GOLD_BLOCK:
                event.setCancelled(true);
                sPlayer.addBalance(-10);
                sPlayer.getTeam().getTower().addBalance(10);
                break;
        }
    }
}
