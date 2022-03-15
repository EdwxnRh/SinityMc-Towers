package de.sirguard.towers.commands;

import com.google.gson.internal.LinkedTreeMap;
import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.utils.ConfigManager;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.objects.Team;
import de.sirguard.towers.utils.PluginMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupCommand implements CommandExecutor {

    // setup command - probably starts with /setup lobby for lobby spawn and /setup map <builder> <size> for creating / saving the map
    // by using /setup map the spectator spawn will be set to the exact position of the cmd executor
    // -> than, /setup team <name> <color> : enables edit mode, hotbar items for setting tower, merchant, banker, upgrades, olli and team spawn
    // /setup spawner : sets spawner

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 1) {
            switch (args[0]) {
                case "map" -> {
                    player.sendMessage("Bitte nutze §b/setup map <BUILDER> <SIZE>");
                    return true;
                }
                case "team" -> {
                    player.sendMessage("Bitte nutze §b/setup team <COLOR>");
                    return true;
                }
                case "tp" -> {
                    player.sendMessage("Bitte nutze §b/setup tp <MAP>");
                    return true;
                }
            }
        }

        if (args.length == 0) {
            player.sendMessage(PluginMessages.getSetupMessage());
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("lobby")) {
            Map<String, Object> lobbyLocationMap = ConfigManager.getLocationToMap(player.getLocation());

            ConfigManager.mapsConfig.put("lobby", lobbyLocationMap);
            ConfigManager.saveMapConfig();
            player.sendMessage(Towers.prefix + "Du hast die Wartelobby gesetzt.");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
            String worldName = args[1];

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                if (new File(worldName).exists()) {
                    player.sendMessage(Towers.prefix + "Lade die Welt...");

                    world = Bukkit.createWorld(new WorldCreator(worldName));
                    world.setDifficulty(Difficulty.NORMAL);
                    world.setTime(12000);
                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

                    for (Entity entity : world.getEntities()) {
                        if(!(entity instanceof Mob) || entity instanceof Player)
                            continue;

                        entity.remove();
                    }

                    player.sendMessage(Towers.prefix + "Die Welt wurde erfolgreich geladen.");
                } else {
                    player.sendMessage(Towers.error + "Die Welt existiert nicht!");
                    return true;
                }
            }

            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("map")) {
            Map<String, Object> worldMap = new LinkedTreeMap<>();

            if (ConfigManager.mapsConfig.containsKey("maps")) {
                worldMap = ((Map) ConfigManager.mapsConfig.get("maps"));
            }

            if (worldMap.containsKey(player.getWorld().getName())) {
                player.sendMessage(Towers.error + "Eine Welt mit diesem Namen existiert bereits!");
                return true;
            }

            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                player.sendMessage(Towers.error + "Bitte nutze eine Ganzzahl!");
                return true;
            }

            Map<String, Object> worldInfoMap = new LinkedTreeMap<>();

            Map<String, Object> spectatorLocationMap = ConfigManager.getLocationToMap(player.getLocation());

            worldInfoMap.put("builder", args[1]);
            worldInfoMap.put("size", Integer.parseInt(args[2]));
            worldInfoMap.put("spectatorLocation", spectatorLocationMap);

            worldMap.put(player.getWorld().getName(), worldInfoMap);

            ConfigManager.mapsConfig.put("maps", worldMap);
            ConfigManager.saveMapConfig();

            player.sendMessage(Towers.prefix + "Die Map §b" + player.getWorld().getName() + " §7wurde erfolgreich gespeichert.");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("team")) {
            if (!Teams.isPossible(args[1])) {
                player.sendMessage(Towers.error + "Diese Teamfarbe exisitiert nicht!");
                return true;
            }

            Map<String, Object> maps = (Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());

            if (!maps.containsKey(player.getWorld().getName())) {
                player.sendMessage(Towers.error + "Die Welt muss zuerst gespeichert werden!");
                return true;
            }

            Map<String, Object> map = (Map<String, Object>) maps.get(player.getWorld().getName());

            if (map.containsKey("teams")) {
                ArrayList<Map<String, Object>> teams = (ArrayList<Map<String, Object>>) map.get("teams");

                for (Map<String, Object> team : teams) {
                    if (((String) team.get("name")).equalsIgnoreCase(args[1])) {
                        player.sendMessage(Towers.error + "Das Team existiert bereits!");
                        return true;
                    }
                }
            }

            Team currentTeam = new Team(args[1]);

            boolean inEditMode = GameManager.playersInTeamsEditMode.containsKey(player);

            for (HashMap<String, Object> value : GameManager.playersInTeamsEditMode.values()) {
                if (((Team) value.get("team")).getName().equalsIgnoreCase(args[1]) || inEditMode) {
                    player.sendMessage(Towers.error + "Entweder du oder jemand anders bearbeitet gerade dieses Team. Um das Problem zu beheben, versuche zu reconnecten.");
                    return true;
                }
            }

            int actionID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Towers.plugin, () -> player.sendActionBar(Component.text("§6/setup cancel §7| §6/setup save")), 0, 40);

            ItemStack[] preInv = player.getInventory().getContents();
            player.getInventory().clear();

            GameManager.playersInTeamsEditMode.put(player, new HashMap<>(Map.of("team", currentTeam, "actionID", actionID, "preInv", preInv)));

            ItemMeta itemMeta;

            ItemStack teamSpawn = new ItemStack(Material.PLAYER_HEAD);
            itemMeta = teamSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Team-Spawn"));
            teamSpawn.setItemMeta(itemMeta);

            ItemStack bankerSpawn = new ItemStack(Material.GOLD_INGOT);
            itemMeta = bankerSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Bankier"));
            bankerSpawn.setItemMeta(itemMeta);

            ItemStack upgradesSpawn = new ItemStack(Material.AMETHYST_SHARD);
            itemMeta = upgradesSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Upgrades"));
            upgradesSpawn.setItemMeta(itemMeta);

            ItemStack merchantSpawn = new ItemStack(Material.EMERALD);
            itemMeta = merchantSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Händler"));
            merchantSpawn.setItemMeta(itemMeta);

            ItemStack towerAxe = new ItemStack(Material.IRON_AXE);
            itemMeta = towerAxe.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Tower"));
            itemMeta.lore(List.of(Component.text("§6Rechts-Klick: §7Location 1"), Component.text("§6Links-Klick: §7Location 2")));
            towerAxe.setItemMeta(itemMeta);

            ItemStack chefSpawn = new ItemStack(Material.CRIMSON_FUNGUS);
            itemMeta = chefSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Olli"));
            chefSpawn.setItemMeta(itemMeta);

            ItemStack shroomieSpawn = new ItemStack(Material.WARPED_FUNGUS);
            itemMeta = shroomieSpawn.getItemMeta();
            itemMeta.displayName(Component.text("§6Setze Bauarbeiter"));
            shroomieSpawn.setItemMeta(itemMeta);

            player.getInventory().setItem(0, teamSpawn);
            player.getInventory().setItem(1, bankerSpawn);
            player.getInventory().setItem(2, upgradesSpawn);
            player.getInventory().setItem(3, merchantSpawn);
            player.getInventory().setItem(6, towerAxe);
            player.getInventory().setItem(7, chefSpawn);
            player.getInventory().setItem(8, shroomieSpawn);

        } else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
            if (!GameManager.playersInTeamsEditMode.containsKey(player)) {
                player.sendMessage(Towers.error + "Du bist nicht im Edit-Mode!");
                return true;
            }

            player.sendMessage(Towers.prefix + "Alle Bearbeitungen wurden verworfen!");

            ItemStack[] preInv = (ItemStack[]) GameManager.playersInTeamsEditMode.get(player).get("preInv");
            int actionID = (int) GameManager.playersInTeamsEditMode.get(player).get("actionID");

            player.getInventory().setContents(preInv);
            player.updateInventory();
            Bukkit.getScheduler().cancelTask(actionID);
            GameManager.playersInTeamsEditMode.remove(player);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
            if (!GameManager.playersInTeamsEditMode.containsKey(player)) {
                player.sendMessage(Towers.error + "Du bist nicht im Edit-Mode!");
                return true;
            }

            Team team = (Team) GameManager.playersInTeamsEditMode.get(player).get("team");

            if (team.getBanker() == null || team.getMerchant() == null || team.getOlli() == null || team.getSpawn() == null ||
                    team.getTower() == null || team.getUpgrades() == null || team.getTower().getLocation1() == null ||
                    team.getTower().getLocation2() == null) {
                player.sendMessage(Towers.error + "Es wurden nicht alle Positionen gesetzt!");
            }

            Location teamSpawnLocation = team.getSpawn();

            Map<String, Object> maps = (Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());
            Map<String, Object> map = (Map<String, Object>) maps.get(teamSpawnLocation.getWorld().getName());

            ArrayList<Map> list = new ArrayList<>();

            if (map.containsKey("teams"))
                list = (ArrayList<Map>) map.get("teams");

            list.add(Map.of(
                    "name", team.getName(),
                    "spawnLocation", ConfigManager.getLocationToMap(teamSpawnLocation),
                    "banker", Map.of(
                            "location", ConfigManager.getLocationToMap(team.getBanker().getLocation())
                    ),
                    "merchant", Map.of(
                            "location", ConfigManager.getLocationToMap(team.getMerchant().getLocation())
                    ),
                    "olli", Map.of(
                            "location", ConfigManager.getLocationToMap(team.getOlli().getLocation())
                    ),
                    "upgrades", Map.of(
                            "location", ConfigManager.getLocationToMap(team.getUpgrades().getLocation())
                    ),
                    "tower", Map.of(
                            "location1", ConfigManager.getLocationToMap(team.getTower().getLocation1()),
                            "location2", ConfigManager.getLocationToMap(team.getTower().getLocation2()),
                            "shroomieLocation", ConfigManager.getLocationToMap(team.getTower().getShroomieLocation())
                    )
            ));

            map.put("teams", list);

            maps.put(teamSpawnLocation.getWorld().getName(), map);

            ConfigManager.mapsConfig.put("maps", maps);
            ConfigManager.saveMapConfig();

            ItemStack[] preInv = (ItemStack[]) GameManager.playersInTeamsEditMode.get(player).get("preInv");
            int actionID = (int) GameManager.playersInTeamsEditMode.get(player).get("actionID");

            player.getInventory().setContents(preInv);
            player.updateInventory();
            Bukkit.getScheduler().cancelTask(actionID);
            GameManager.playersInTeamsEditMode.remove(player);
            player.sendMessage(Towers.prefix + "Alle Änderungen wurden gespeichert!");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("spawner")) {
            Map<String, Object> maps = (Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());
            Map<String, Object> map = (Map<String, Object>) maps.get(player.getWorld().getName());

            ArrayList<Map> list = new ArrayList<>();

            if (map.containsKey("spawners"))
                list = (ArrayList<Map>) map.get("spawners");

            Map<String, Object> spawnerLocationMap = ConfigManager.getLocationToMap(player.getLocation().getBlock().getLocation().subtract(0, 1, 0));

            list.add(spawnerLocationMap);

            map.put("spawners", list);
            player.sendMessage(Towers.prefix + "Du hast erfolgreich einen Spawner gesetzt.");

            ConfigManager.saveMapConfig();
        } else if(args.length == 1 && args[0].equalsIgnoreCase("build")) {
            if(GameManager.playersInBuildMode.contains(player)) {
                GameManager.playersInBuildMode.remove(player);
                player.sendMessage(Towers.prefix + "Du bist nun nicht mehr im Baumodus!");
            } else {
                GameManager.playersInBuildMode.add(player);
                player.sendMessage(Towers.prefix + "Du bist nun im Baumodus!");
            }
        }
        return true;
    }
}