package de.sirguard.towers.listener;

import de.sirguard.towers.Towers;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.utils.ItemBuilder;
import de.sirguard.towers.objects.SPlayer;
import de.sirguard.towers.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Objects;
import java.util.UUID;

public class UpgradesListener implements Listener {
    private final String GUI_NAME = "§aUpgrades Shop";
    private final String GENERAL_UPGRADES = "§aGeneral Upgrades Shop";
    private final String TOWER_UPGRADES = "§aTower Upgrades Shop";
    private final String BASE_UPGRADES = "§aBase Upgrades Shop";
    private final int frugatility_price = 60;
    private final int speed_price = 20;
    private final int resistance_price = 300;
    private final int tower_warper_price = 75;
    private final int guard_price = 350;
    private final int spy_price = 350;

    private final int efficiency_price = 15;
    private final int swiftness_price = 150;
    private final int armor_price = 150;
    private final int drop_bonus_price = 50;
    private final int quick_spawn_price = 75;
    private final int security_price = 120;
    private final int discount_price = 150;

    private final int simpleDeposit_price = 60;
    private final int heal_price = 120;
    private final int modifiedStrength_price = 200;
    private final int slowness_price = 120;
    private final int weakness_price = 120;



    public void openGUI(Player player) {
        SPlayer sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());
        Team team = sPlayer.getTeam();


       // inv.setItem(1, new ItemBuilder(Material.IRON_PICKAXE).setDisplayName("§9Efficiency" + team.upg.get("effi")).setLore("§3Upgrade your pickaxe with efficiency to mine faster!", "§7Price: §6").build());

        player.openInventory(generalUpgrades(sPlayer));
    }

    @EventHandler
    public void onUpgradesHandlerClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager upgrades)) return;
        if (Objects.equals(upgrades.getCustomName(), "§aUpgrades")) {
            event.setCancelled(true);
            openGUI(event.getPlayer());
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || event.getClickedInventory() == null ||
                event.getCurrentItem() == null) return;

        if (!event.getView().getTitle().equals(GENERAL_UPGRADES) &&  !event.getView().getTitle().equals(TOWER_UPGRADES) &&  !event.getView().getTitle().equals(BASE_UPGRADES) ) return;
        SPlayer sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());
        event.setCancelled(true);
        switch (event.getCurrentItem().getType()) {

            case BRICKS:
                player.openInventory(generalUpgrades(sPlayer));
                break;
            case GOLDEN_CHESTPLATE:
                player.openInventory(towerUpgrades(sPlayer));
                break;
            case BEACON:
                player.openInventory(baseUpgrades(sPlayer));
                break;
            default:
                break;
        }
        if (event.getView().getTitle().equals(GENERAL_UPGRADES)) {
            switch (event.getCurrentItem().getType()){
                case IRON_PICKAXE:
                    upgrade(player, efficiency_price, "efficiency");
                    break;
                case SUGAR:
                    upgrade(player, swiftness_price, "swiftness");
                    break;
                case COOKIE:
                    upgrade(player,drop_bonus_price, "drop bonus");
                    break;
                case CLOCK:
                    upgrade(player, quick_spawn_price, "quick spawn");
                    break;
                case BEDROCK:
                    upgrade(player, security_price, "security");
                    break;
                case GOLD_INGOT:
                    upgrade(player, discount_price, "discount");
                    break;

                default: break;
            }
        } else if (event.getView().getTitle().equals(BASE_UPGRADES)) {
            switch (event.getSlot()){
                case 0:
                    upgrade(player, simpleDeposit_price, "deposit");
                    break;
                case 1:
                    upgrade(player, heal_price, "heal");
                    break;
                case 2:
                    upgrade(player, modifiedStrength_price, "strength");
                    break;
                case 3:
                    upgrade(player, slowness_price, "slowness");
                    break;
                case 4:
                    upgrade(player, weakness_price, "weakness");
                    break;

                default: break;
            }
        } else if (event.getView().getTitle().equals(TOWER_UPGRADES)) {
            switch (event.getCurrentItem().getType()){
                case GOLD_NUGGET:
                    upgrade(player, frugatility_price, "frugatility");
                    break;
                case VILLAGER_SPAWN_EGG:
                    upgrade(player,speed_price, "speed");
                    break;
                case OBSIDIAN:
                   upgrade(player, resistance_price, "resistance");
                    break;
                case LIGHT_WEIGHTED_PRESSURE_PLATE:
                    upgrade(player, tower_warper_price, "tower warper");
                    break;
                case NOTE_BLOCK:
                    upgrade(player, guard_price, "guard");
                    break;
                case ENDER_EYE:
                    upgrade(player, spy_price, "spy");
                    break;

                default: break;
            }
        }
    }

    private Inventory generalUpgrades(SPlayer player) {
        Inventory inv = Bukkit.createInventory(null, 9*3, GENERAL_UPGRADES);

        Material mat = Material.BLACK_STAINED_GLASS_PANE;
        for (int i = 9; i < 18; i++) {
            inv.setItem(i, new ItemStack(mat));
        }
        inv.setItem(18, new ItemStack(mat));
        inv.setItem(26, new ItemStack(mat));

        ItemStack generalItem = new ItemBuilder(Material.BRICKS).setDisplayName("§aGeneral Upgrades").setEnchantment(Enchantment.DURABILITY, 1, false).build();
        inv.setItem(19, generalItem);
        ItemStack teamItem = new ItemBuilder(Material.GOLDEN_CHESTPLATE).setDisplayName("§aTeam Upgrades").build();
        inv.setItem(20, teamItem);
        ItemStack baseItem = new ItemBuilder(Material.BEACON).setDisplayName("§aBase Upgrades").build();
        inv.setItem(21, baseItem);

        ItemStack effi = new ItemBuilder(Material.IRON_PICKAXE).setDisplayName("§9Efficiency I").setLore("§3Dein Team erhält §9Efficiency I §3auf der Spitzhacke", "\n", "§7Preis: §6" + efficiency_price).build();
        inv.setItem(0, effi);
        ItemStack swiftness = new ItemBuilder(Material.SUGAR).setDisplayName("§9Swiftness I").setLore("§3Dein Team erhält §9Schnelligkeit I", "\n", "§7Preis: §6" + swiftness_price).build();
        inv.setItem(1, swiftness);
        ItemStack armor = new ItemBuilder(Material.COOKIE).setDisplayName("§9Drop Bonus I").setLore("§3Dein Team hast eine §910% §3Chance für mehr Ziegelblöcke", "\n", "§7Preis: §6" + armor_price).build();
        inv.setItem(2, armor);
        ItemStack quick_spawn = new ItemBuilder(Material.CLOCK).setDisplayName("§9Quick Spawn I").setLore("§3Maschinen produzieren §920% §3schneller", "\n", "§7Preis: §6" + quick_spawn_price).build();
        inv.setItem(3, quick_spawn);
        ItemStack security = new ItemBuilder(Material.BEDROCK).setDisplayName("§9Security").setLore("§3Gegner werden in Maschinen erblinden", "\n", "§7Preis: §6" + security_price).build();
        inv.setItem(4, security);
        ItemStack discount = new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§9Discount").setLore("§3Die Kosten im Shop werden um §930% §3gesenkt", "\n", "§7Preis: §6" + discount_price).build();
        inv.setItem(5, discount);


        return inv;
    }

    private Inventory baseUpgrades(SPlayer player) {
        Inventory inv = Bukkit.createInventory(null, 9*3, BASE_UPGRADES);
        Material mat = Material.BLACK_STAINED_GLASS_PANE;
        for (int i = 9; i < 18; i++) {
            inv.setItem(i, new ItemStack(mat));
        }
        inv.setItem(18, new ItemStack(mat));
        inv.setItem(26, new ItemStack(mat));

        ItemStack generalItem = new ItemBuilder(Material.BRICKS).setDisplayName("§aGeneral Upgrades").build();
        inv.setItem(19, generalItem);
        ItemStack teamItem = new ItemBuilder(Material.GOLDEN_CHESTPLATE).setDisplayName("§aTower Upgrades").build();
        inv.setItem(20, teamItem);
        ItemStack baseItem = new ItemBuilder(Material.BEACON).setEnchantment(Enchantment.DURABILITY, 1, false).setDisplayName("§aBase Upgrades").build();
        inv.setItem(21, baseItem);

        ItemStack paper = new ItemBuilder(Material.PAPER).setDisplayName("§9Simple Deposit").setLore("§3Zahle alle deine Ziegel mit", "§3Linksklick ein", "\n", "§7Preis: §6" + simpleDeposit_price ).build();
        inv.setItem(0, paper);

        ItemStack healpotion = new ItemStack(Material.POTION);
        PotionMeta healpotionMeta = ((PotionMeta) healpotion.getItemMeta());
        //healpotionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, false));
        healpotionMeta.setLore(null);
        healpotionMeta.setColor(Color.RED);
        healpotion.setItemMeta(healpotionMeta);

        ItemStack heal_item = new ItemBuilder(healpotion).setDisplayName("§9Heilung").setLore("§3Dein Team erhält Regenerierung", "\n", "§7Preis: §6" + heal_price ).build();
        inv.setItem(1, heal_item);

        ItemStack strengthpotion = new ItemStack(Material.POTION);
        PotionMeta strengthpotionMeta = ((PotionMeta) strengthpotion.getItemMeta());
        strengthpotionMeta.setLore(null);
        strengthpotionMeta.setColor(Color.PURPLE);
        strengthpotion.setItemMeta(strengthpotionMeta);

        ItemStack strength_item = new ItemBuilder(strengthpotion).setDisplayName("§9Stärke").setLore("§3Dein Team erhält Stärke", "\n", "§7Preis: §6" + modifiedStrength_price ).build();
        inv.setItem(2, strength_item);

        ItemStack slownesspotion = new ItemStack(Material.POTION);
        PotionMeta slownesspotionMeta = ((PotionMeta) slownesspotion.getItemMeta());
        slownesspotionMeta.setLore(null);
        slownesspotionMeta.setColor(Color.TEAL);
        slownesspotion.setItemMeta(slownesspotionMeta);

        ItemStack slowness_item = new ItemBuilder(slownesspotion).setDisplayName("§9Langsamkeit)").setLore("§3Gegner erhalten Langsamkeit", "\n", "§7Preis: §6" + slowness_price ).build();
        inv.setItem(3, slowness_item);

        ItemStack weaknesspotion = new ItemStack(Material.POTION);
        PotionMeta weaknesspotionMeta = ((PotionMeta) weaknesspotion.getItemMeta());
        weaknesspotionMeta.setLore(null);
        weaknesspotionMeta.setColor(Color.GRAY);
        weaknesspotion.setItemMeta(weaknesspotionMeta);

        ItemStack weakness_item = new ItemBuilder(weaknesspotion).setDisplayName("§9Schwäche").setLore("§3Gegner erhalten Schwäche", "\n", "§7Preis: §6" + weakness_price ).build();
        inv.setItem(4, weakness_item);








        return inv;
    }

    private Inventory towerUpgrades(SPlayer player) {
        Inventory inv = Bukkit.createInventory(null, 9*3, TOWER_UPGRADES);
        Material mat = Material.BLACK_STAINED_GLASS_PANE;

        for (int i = 9; i < 18; i++) {
            inv.setItem(i, new ItemStack(mat));
        }
        inv.setItem(18, new ItemStack(mat));
        inv.setItem(26, new ItemStack(mat));


        ItemStack generalItem = new ItemBuilder(Material.BRICKS).setDisplayName("§aGeneral Upgrades").build();
        inv.setItem(19, generalItem);
        ItemStack teamItem = new ItemBuilder(Material.GOLDEN_CHESTPLATE).setDisplayName("§aTower Upgrades").setEnchantment(Enchantment.DURABILITY, 1, false).build();
        inv.setItem(20, teamItem);
        ItemStack baseItem = new ItemBuilder(Material.BEACON).setDisplayName("§aBase Upgrades").build();
        inv.setItem(21, baseItem);

        ItemStack goldnugget = new ItemBuilder(Material.GOLD_NUGGET).setDisplayName("§9Frugality I").setLore("§3Die Bauarbeiter spaaren mit einer §910% §3Chance einen Ziegelblock ein", "\n", "§7Preis: §6" + frugatility_price).build();
        inv.setItem(0, goldnugget);

        ItemStack vil = new ItemBuilder(Material.VILLAGER_SPAWN_EGG).setDisplayName("§9Speed I").setLore("§3Die Bauarbeiter bauen §910% §3schneller", "\n", "§7Preis: §6" + speed_price).build();
        inv.setItem(1, vil);

        ItemStack obsidian = new ItemBuilder(Material.OBSIDIAN).setDisplayName("§9Resistance I").setLore("§3Der Turm kann nicht so schnell zerstört werden", "\n", "§7Preis: §6" + resistance_price).build();
        inv.setItem(2, obsidian);

        ItemStack goldplate = new ItemBuilder(Material.LIGHT_WEIGHTED_PRESSURE_PLATE).setDisplayName("§9Tower Warper I").setLore("§3Dein Team kann sich alle §930s §3auf den Turm teleportieren", "\n", "§7Preis: §6" + tower_warper_price).build();
        inv.setItem(3, goldplate);

        ItemStack noteblock = new ItemBuilder(Material.NOTE_BLOCK).setDisplayName("§9Guard").setLore("§3Wenn sich Gegner dem Turm nähern, \n werden diese aufleuchten und dein Team erhält eine Warnung", "\n", "§7Preis: §6" + guard_price).build();
        inv.setItem(4, noteblock);

        ItemStack endereye = new ItemBuilder(Material.ENDER_EYE).setDisplayName("§9Spy").setLore("§3Dein Team kann den Fortschritt der anderen Teams sehen", "\n", "§7Preis: §6" + spy_price).build();
        inv.setItem(5, endereye);

        return inv;
    }


    private void upgrade(Player player, int upgrade_price, String upgrade_id) {
        upgrade(player, upgrade_price, upgrade_id, "I");
    }

    private void upgrade(Player player, int upgrade_price, String upgrade_id, String upgrade_strength) {
        SPlayer sPlayer = GameManager.playersMap.get(player.getUniqueId().toString());
        if (sPlayer.getBalance() + sPlayer.getTeam().getBalance() >= upgrade_price) {
            if (upgrade_price <= sPlayer.getBalance()) {
                sPlayer.addBalance(-upgrade_price);
            } else {
                int money = sPlayer.getBalance() - upgrade_price;
                sPlayer.setBalance(0);
                sPlayer.getTeam().addBalance(money);
            }
            sPlayer.getTeam().upg.put(upgrade_id, 1);
            for (UUID teamPlayerID : sPlayer.getTeam().getPlayers()) {
                Player teamPlayer = Bukkit.getPlayer(teamPlayerID);
                teamPlayer.sendMessage(Towers.prefix + "§b" + player.getName() + " §9hat " + upgrade_id + " " + upgrade_strength + " §9gekauft.");
            }
        } else {
            player.sendMessage(Towers.prefix + "Dieses Upgrade kannst du dir leider nicht leisten!");
        }
    }

}
