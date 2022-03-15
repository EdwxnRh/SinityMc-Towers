package de.sirguard.towers.objects;

import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Team {
    private final String name;
    private Location spawn;
    private final ArrayList<UUID> players;
    private final ArrayList<ItemStack> items;
    private int slot;

    private ItemStack helmet, chestplate, leggings, boots;
    private Tower tower;
    private Banker banker;
    private Upgrades upgrades;
    private Merchant merchant;
    private Olli olli;

    private int balance;
    public HashMap<String, Integer> upg = new HashMap<>();

    public Team(String name) {
        this.name = name;
        this.slot = slot;
        this.players = new ArrayList<>();
        this.items = new ArrayList<>();
        startItems();
        startArmor();


        int defaultValue = 0;
        upg.put("frugatility", defaultValue);
        upg.put("speed", defaultValue);
        upg.put("prot", defaultValue);
        upg.put("resistance", defaultValue);
        upg.put("tower warper", defaultValue);
        upg.put("guard", defaultValue);
        upg.put("spy", defaultValue);

        upg.put("efficiency", defaultValue);
        upg.put("swiftness", defaultValue);
        upg.put("drop bonus", defaultValue);
        upg.put("quick spawn", defaultValue);
        upg.put("security", defaultValue);
        upg.put("discount", defaultValue);

    }

    private void startItems() {
        addItem(new ItemBuilder(Material.IRON_PICKAXE).setUnbreakable(true).build());

    }

    private void startArmor() {
        setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(Teams.getTeamByDisplayName(name).getLeatherColor()).setUnbreakable(true).build());

    }

    public void setArmor(Player player) {
        if (helmet !=  null) {
            player.getInventory().setHelmet(helmet);
        }
        if (chestplate !=  null) {
            player.getInventory().setChestplate(chestplate);
        }
        if (leggings !=  null) {
            player.getInventory().setLeggings(leggings);
        }
        if (boots !=  null) {
            player.getInventory().setBoots(boots);
        }

    }

    public void addPlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getScoreboard().getTeam(name).addEntity(player);
            //Fix colors when rejoin
        }

        this.players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getScoreboard().getTeam(name).removeEntity(player);
        }

        this.players.remove(player.getUniqueId());
    }

    public static Team getTeamByPlayer(Player player) {
        return GameManager.playersMap.get(player.getUniqueId().toString()).getTeam();
    }

    public static Team getTeamByName(String team) {
        List<Team> res = Arrays.stream(GameManager.teams).filter(t -> t.getName().equals(team)).toList();
        for(Team t : res){
            return t;
        }
        return null;
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

    public void removeItem(ItemStack item) {
        items.remove(item);
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void addBalance() {
        this.balance++;
    }

    public void addBalance(int balance) {
        this.balance += balance;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public void setBanker(Banker banker) {
        this.banker = banker;
    }

    public void setUpgrades(Upgrades upgrades) {
        this.upgrades = upgrades;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setOlli(Olli olli) {
        this.olli = olli;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn() {
        return spawn;
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public Tower getTower() {
        return tower;
    }

    public Banker getBanker() {
        return banker;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public Olli getOlli() {
        return olli;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static Team getTeamBySlot(int slot) {
        for (Team team : GameManager.teams) {
           if (team.getSlot() == slot){
               return team;
           }
        }
        return null;
    }
}
