package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.utils.ItemBuilder;
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

public class BankerListener implements Listener {
    private final String title = "§6Bankier";


    public void openGui(Player player) {
        player.openInventory(bankerinv());
    }

    @EventHandler
    public void onOlliClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager olli)) return;

        if (Objects.equals(olli.getCustomName(), "§aBankier")) {
            event.setCancelled(true);
            openGui(event.getPlayer());
        }
    }

    public Inventory bankerinv() {
        Inventory inv = Bukkit.createInventory(null, 9 * 1, title);

        ItemStack dep2 = new ItemBuilder(Material.COOKIE).setDisplayName("§62 Ziegel").setLore("§7Zahle 2 Ziegel ein").build();
        inv.setItem(0, dep2);
        ItemStack dep8 = new ItemBuilder(Material.COOKIE).setDisplayName("§68 Ziegel").setLore("§7Zahle 8 Ziegel ein").build();
        inv.setItem(2, dep8);
        ItemStack dep32 = new ItemBuilder(Material.COOKIE).setDisplayName("§632 Ziegel").setLore("§7Zahle 32 Ziegel ein").build();
        inv.setItem(4, dep32);
        ItemStack dep64 = new ItemBuilder(Material.COOKIE).setDisplayName("§664 Ziegel").setLore("§7Zahle 64 Ziegel ein").build();
        inv.setItem(6, dep64);
        ItemStack depblock = new ItemBuilder(Material.COOKIE).setDisplayName("§61 Ziegelblock").setLore("§7Zahle einen Ziegelblock ein", "§71 Ziegelblock = 2 Ziegel").build();
        inv.setItem(8, depblock);
        return inv;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(title)) return;
        SPlayer sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());
        event.setCancelled(true);
        switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§62 Ziegel":
                if (sPlayer.getBalance() >= 2) {
                    sPlayer.addBalance(-2);
                    sPlayer.getTeam().addBalance(2);
                } else {
                    sPlayer.getTeam().addBalance(sPlayer.getBalance());
                    sPlayer.setBalance(0);
                }
                break;
            case "§68 Ziegel":
                if (sPlayer.getBalance() >= 8) {
                    sPlayer.addBalance(-8);
                    sPlayer.getTeam().addBalance(8);
                } else {
                    sPlayer.getTeam().addBalance(sPlayer.getBalance());
                    sPlayer.setBalance(0);
                }
                break;
            case "§632 Ziegel":
                if (sPlayer.getBalance() >= 32) {
                    sPlayer.addBalance(-32);
                    sPlayer.getTeam().addBalance(32);
                } else {
                    sPlayer.getTeam().addBalance(sPlayer.getBalance());
                    sPlayer.setBalance(0);
                }
                break;
            case "§664 Ziegel":
                if (sPlayer.getBalance() >= 64) {
                    sPlayer.addBalance(-64);
                    sPlayer.getTeam().addBalance(64);
                } else {
                    sPlayer.getTeam().addBalance(sPlayer.getBalance());
                    sPlayer.setBalance(0);
                }
                break;
            case "§61 Ziegelblock":
                if (event.getClick().isShiftClick()) {
                    if (player.getInventory().contains(Towers.ziegelBlock)) {
                        int amount = 0;
                        for (int i = 0; i < 35; i++) {
                            if (Objects.requireNonNull(player.getInventory().getItem(i)).getType().equals(Towers.ziegelBlock)) {
                                amount = Objects.requireNonNull(player.getInventory().getItem(i)).getAmount();
                                player.getInventory().clear(i);
                            }
                        }
                        sPlayer.addBalance(amount * 2);
                    }
                } else {
                    if (player.getInventory().contains(Towers.ziegelBlock)) {
                        player.getInventory().remove(new ItemStack(Towers.ziegelBlock, 1));
                        sPlayer.addBalance(2);
                    }
                }
                break;
            default:
                break;
        }
    }

}
