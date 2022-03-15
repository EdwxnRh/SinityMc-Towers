package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.objects.Map;
import de.sirguard.towers.objects.Team;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PreGameSelectListener implements Listener {

    static Inventory teamSelection;
    // Inventory kitSelectionPageOne;
    // Inventory kitSelectionPageTwo;
    public static Inventory voting;

    public static void setTeamItems(int teamAmount, List<Integer> positions){
        for (int i = 0; i < teamAmount; i++) {
            Teams team = Teams.values()[i];
            ItemStack teamItem = new ItemBuilder(team.getCandle()).setDisplayName(team.getTitle() + " §7(" + Team.getTeamByName(team.getName()).getPlayers().size() + "/" + GameManager.teamSize + ")").build();
            teamSelection.setItem(positions.get(i), teamItem);
            Team.getTeamByName(team.getName()).setSlot(positions.get(i));
        }
    }

    public static void setMapItems(List<Integer> positions){
        int i = 0;
        for (Map map : GameManager.maps.values()) {
            ItemBuilder mapItem = new ItemBuilder(Material.GLOBE_BANNER_PATTERN).setDisplayName("§6" + map.getName() + "§7 by §6" + map.getBuilder()).setItemFlag(ItemFlag.HIDE_POTION_EFFECTS);
            mapItem.setLore("§7Ausgewählt von §6" + (GameManager.countVotes(map.getName()) == 1 ? "einem §7Spieler" : GameManager.countVotes(map.getName()) + " §6Spielern"));
            voting.setItem(positions.get(i), mapItem.build());
            i++;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (GameManager.gameState == GameState.RUNNING || GameManager.playersInBuildMode.contains(event.getPlayer()))
            return;

        Player player = event.getPlayer();

        event.setCancelled(true);

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        switch (player.getItemInHand().getType()) {
            case SUNFLOWER -> {
                teamSelection = Bukkit.createInventory(null, GameManager.teamAmount >= 9 ? 4*9 : 3*9, "§8Teamauswahl");
                for (int i = 0; i < 9; i++) {
                    teamSelection.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }
                for (int i = 18; i < 27; i++) {
                    teamSelection.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }
                switch (GameManager.teamAmount) {
                    case 2 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(12);
                        pos.add(14);
                        setTeamItems(2, pos);
                    }
                    case 3 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(11);
                        pos.add(13);
                        pos.add(15);
                       setTeamItems(3, pos);
                    }
                    case 4 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(12);
                        pos.add(14);
                        pos.add(16);
                        setTeamItems(4, pos);
                    }
                    case 5 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(12);
                        pos.add(13);
                        pos.add(14);
                        pos.add(16);
                        setTeamItems(5, pos);
                    }
                    case 6 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        setTeamItems(6, pos);
                    }
                    case 7 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(9);
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(17);
                        setTeamItems(7, pos);
                    }
                    case 8 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(9);
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(13);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(17);
                        setTeamItems(8, pos);
                    }
                    case 9 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(12);
                        pos.add(14);
                        pos.add(16);
                        pos.add(18);
                        pos.add(20);
                        pos.add(22);
                        pos.add(24);
                        pos.add(26);
                        setTeamItems(9, pos);
                    }
                    case 10 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(11);
                        pos.add(12);
                        pos.add(13);
                        pos.add(14);
                        pos.add(15);
                        pos.add(20);
                        pos.add(21);
                        pos.add(22);
                        pos.add(23);
                        pos.add(24);
                        setTeamItems(10, pos);
                    }
                    case 11 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(20);
                        pos.add(21);
                        pos.add(22);
                        pos.add(23);
                        pos.add(24);
                        setTeamItems(11, pos);
                    }
                    case 12 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(19);
                        pos.add(20);
                        pos.add(21);
                        pos.add(23);
                        pos.add(24);
                        pos.add(25);
                        setTeamItems(12, pos);
                    }
                    default -> {}
                }

                player.openInventory(teamSelection);
            }
            case NETHER_STAR -> {
                if (GameManager.gameState != GameState.VOTING) {
                    player.sendMessage(Towers.prefix + "Die Voting-Phase ist bereits zuende!");
                    return;
                }

                voting = Bukkit.createInventory(null, 3*9, "§8Map Voting");

                for (int i = 0; i < 9; i++) {
                    voting.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }
                for (int i = 18; i < 27; i++) {
                    voting.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                }

                switch (GameManager.maps.size()) {
                    case 1 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(13);
                        setMapItems(pos);
                    }
                    case 2 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(12);
                        pos.add(14);
                        setMapItems(pos);
                    }
                    case 3 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(11);
                        pos.add(13);
                        pos.add(15);
                        setMapItems(pos);
                    }
                    case 4 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(12);
                        pos.add(14);
                        pos.add(16);
                        setMapItems(pos);
                    }
                    case 5 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(11);
                        pos.add(12);
                        pos.add(13);
                        pos.add(14);
                        pos.add(15);
                        setMapItems(pos);
                    }
                    case 6 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        setMapItems(pos);
                    }
                    case 7 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(9);
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(17);
                        setMapItems(pos);
                    }
                    case 8 -> {
                        List<Integer> pos = new ArrayList<>();
                        pos.add(9);
                        pos.add(10);
                        pos.add(11);
                        pos.add(12);
                        pos.add(13);
                        pos.add(14);
                        pos.add(15);
                        pos.add(16);
                        pos.add(17);
                        setMapItems(pos);
                    }
                    default -> {}
                }

                player.openInventory(voting);
            }
            /*
            case CHEST -> {
                kitSelectionPageOne = Bukkit.createInventory(null, 9*5, "§b§lKits - Page: " + "1");
                kitSelectionPageTwo = Bukkit.createInventory(null, 9*5, "§b§lKits - Page: " + "2");

                ItemStack arrowLeft = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
                SkullMeta arrowLeftMeta = (SkullMeta) arrowLeft.getItemMeta();
                arrowLeftMeta.setOwner("MHF_ArrowLeft");
                arrowLeftMeta.setDisplayName("§aNext Page");
                arrowLeft.setItemMeta(arrowLeftMeta);

                ItemStack arrowRight = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
                SkullMeta arrowRightMeta = (SkullMeta) arrowLeft.getItemMeta();
                arrowRightMeta.setOwner("MHF_ArrowRight");
                arrowRightMeta.setDisplayName("§aNext Page");
                arrowRight.setItemMeta(arrowRightMeta);


                ItemStack prevPage = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta prevMeta = prevPage.getItemMeta();
                prevMeta.setDisplayName("§cPrevious Page");
                prevPage.setItemMeta(prevMeta);

                ItemStack nextPage = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta nextMeta = nextPage.getItemMeta();
                nextMeta.setDisplayName("§aNext Page");
                nextPage.setItemMeta(nextMeta);

                kitSelectionPageOne.setItem(18, prevPage);
                kitSelectionPageOne.setItem(26, nextPage);
                kitSelectionPageTwo.setItem(18, prevPage);
                kitSelectionPageTwo.setItem(26, nextPage);

                player.openInventory(kitSelectionPageOne);
            }
            */
        }
    }
}
