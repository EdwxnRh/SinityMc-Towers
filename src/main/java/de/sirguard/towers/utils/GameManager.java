package de.sirguard.towers.utils;

import com.google.gson.internal.LinkedTreeMap;
import de.sirguard.towers.Towers;
import de.sirguard.towers.enums.GameState;
import de.sirguard.towers.enums.Teams;
import de.sirguard.towers.objects.*;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


public class GameManager {

    public static GameState gameState = GameState.VOTING;
    public static Team[] teams;
    public static HashMap<Player, HashMap<String, Object>> playersInTeamsEditMode = new HashMap<>();
    public static ArrayList<Player> playersInBuildMode = new ArrayList<>();
    public static HashMap<Player, Map> votedMap = new HashMap<>();
    public static HashMap<String, SPlayer> playersMap = new HashMap<>();
    public static int teamAmount;
    public static int teamSize;
    public static HashMap<String, Map> maps = new HashMap<>();
    public static int countdown = 60;
    private final static int COUNTDOWN_INIT_VALUE = 60;
    private static int schedulerID;
    public static Map map;
    public static ArrayList<Location> spawnerLocations = new ArrayList<>();
    public static boolean forcestarted = false;

    public static void init() {
        teamAmount = (int) (double) ConfigManager.generalConfig.get("teams");
        teamSize = (int) (double) ConfigManager.generalConfig.get("teamSize");

        teams = new Team[teamAmount];


        Bukkit.setMaxPlayers(teamAmount * teamSize);


        loadMapsAndInitTeams();



        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Towers.plugin, () -> {
            if(GameManager.maps.size() == 0) {
                if(countdown % 30 == 0)
                    Bukkit.broadcast(Component.text(Towers.error + "Keine Map konnte gefunden werden. Bitte nutze /setup um eine Map einzurichten."));

                countdown--;

                if(countdown == 0)
                    countdown = COUNTDOWN_INIT_VALUE;
                return;
            }

            int minPlayer = (teamAmount * teamSize) / 2;

            if(teamAmount == 2 && teamSize == 1) {
                minPlayer = 2;
            }

            if(Bukkit.getOnlinePlayers().size() < minPlayer && !forcestarted) {
                if(countdown != COUNTDOWN_INIT_VALUE) {
                    countdown = COUNTDOWN_INIT_VALUE;

                    if(GameManager.map != null && Bukkit.getWorld(GameManager.map.getName()) != null) {
                        Bukkit.unloadWorld(map.getName(), true);
                    }

                    int playerAmount = minPlayer - Bukkit.getOnlinePlayers().size();
                    Bukkit.broadcast(Component.text(Towers.prefix + "§c" + playerAmount + " §7Spieler" + (playerAmount == 1 ? "wird" : "werden") + " benötigt um das Spiel zu starten!"));

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setLevel(0);
                        onlinePlayer.setExp(0);
                    }
                }
            } else {
                if(countdown == 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setLevel(0);
                        player.setExp(0);
                    }

                    initIngamePhase();
                    Bukkit.getScheduler().cancelTask(schedulerID);
                    return;
                }

                switch (countdown) {
                    case 60:
                    case 30:
                    case 10:
                    case 9:
                    case 8:
                    case 7:
                    case 6:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        Bukkit.broadcast(Component.text(Towers.prefix + "Das Spiel startet in §6" + countdown + " §7Sekunde" +
                                (countdown > 1 ? "n" : "") + "." + (countdown > 10 ? " Das Voting endet in §6" +
                                (countdown-10) + " §7Sekunden!" : "")));

                        break;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setLevel(countdown);
                    player.setExp(0);
                }

                countdown--;
                if (!forcestarted) {
                    if (countdown + 1 == 10) {
                        loadVotedMap();
                    }
                }

            }
        }, 0, 20);
    }



    private static void loadMapsAndInitTeams() {
        java.util.Map<String, Object> maps = (java.util.Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());

        for (java.util.Map.Entry<String, Object> entry : maps.entrySet()) {
            String worldName = entry.getKey();

            java.util.Map<String, Object> map = (java.util.Map<String, Object>) entry.getValue();
            String builder = (String) map.get("builder");
            int teamAmount = (int) (double) map.get("size");

            if(teamAmount != GameManager.teamAmount)
                continue;

            Location spectatorLocation = ConfigManager.getLocationOfMap((java.util.Map<String, Object>) map.get("spectatorLocation"));

            GameManager.maps.put(worldName, new Map(worldName, builder, teamAmount, spectatorLocation));
        }

        for (int i = 0; i < GameManager.teamAmount; i++) {
            Teams team = Teams.values()[i];
            teams[i] = new Team(team.getName());
        }
    }

    private static void loadTeams() {
        java.util.Map<String, Object> maps = (java.util.Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) maps.get(GameManager.map.getName());
        ArrayList<java.util.Map<String, Object>> teams = (ArrayList<java.util.Map<String, Object>>) map.get("teams");

        for (java.util.Map<String, Object> entry : teams) {
            for (int i = 0; i < GameManager.teamAmount; i++) {
                Team team = GameManager.teams[i];

                if(team.getName().equalsIgnoreCase(Teams.getTeamByEnumName((String) entry.get("name")).getName())) {
                    team.setSpawn(ConfigManager.getLocationOfMap((java.util.Map<String, Object>) entry.get("spawnLocation")));
                    team.setBanker(new Banker(ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("banker"))
                                    .get("location"))));
                    team.setMerchant(new Merchant(ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("merchant"))
                                    .get("location"))));
                    team.setUpgrades(new Upgrades(ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("upgrades"))
                                    .get("location"))));
                    team.setOlli(new Olli(ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("olli"))
                                    .get("location"))));
                    team.setTower(new Tower(ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("tower"))
                                    .get("location1")), ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("tower"))
                                    .get("location2")),ConfigManager.getLocationOfMap(
                            (java.util.Map<String, Object>) ((java.util.Map<String, Object>) entry.get("tower"))
                                    .get("shroomieLocation")), team));
                }
            }
        }
    }

    public static void loadVotedMap() {
        GameManager.gameState = GameState.IDLE;

        if(votedMap.size() == 0) {
            Random rand = new Random();
            GameManager.map = (Map) maps.values().toArray()[rand.nextInt(maps.size())];
        } else {
            int highestScore = 0;
            HashMap<Map, Integer> mapVotes = new HashMap<>();

            for (Map value : votedMap.values()) {
                mapVotes.putIfAbsent(value, 0);
                mapVotes.put(value, mapVotes.get(value) + 1);
            }

            for (java.util.Map.Entry<Map, Integer> entry : mapVotes.entrySet()) {
                if(entry.getValue() > highestScore) {
                    GameManager.map = entry.getKey();
                    highestScore = entry.getValue();
                }
            }
        }

        if(Bukkit.getWorld(GameManager.map.getName()) == null) {
            World world = Bukkit.createWorld(new WorldCreator(GameManager.map.getName()));
            world.setDifficulty(Difficulty.NORMAL);
            world.setTime(12000);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

            for (Entity entity : world.getEntities()) {
                if (entity instanceof Player) return;
                entity.remove();
            }
        } else {
            for (Entity entity : Bukkit.getWorld(GameManager.map.getName()).getEntities()) {
                if (entity instanceof Player) return;
                entity.remove();
            }
        }

        loadSpawnerLocations();
        loadTeams();

        Bukkit.broadcast(Component.text(Towers.prefix + "§7Die Map " + "§6" + GameManager.map.getName() + "§7 - von §6" + GameManager.map.getBuilder() + " §7wird gespielt!"));
    }

    public static void initIngamePhase() {
        GameManager.gameState = GameState.RUNNING;
        loop1: for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(GameManager.playersMap.get(onlinePlayer.getUniqueId().toString()).getTeam() != null)
                continue loop1;

            Team team = getSmallestTeam();

            team.addPlayer(onlinePlayer);
            GameManager.playersMap.get(onlinePlayer.getUniqueId().toString()).setTeam(team);
        }

        for (Team team : GameManager.teams) {
            team.getUpgrades().spawn();
            // team.getMerchant().spawn();
            team.getOlli().spawn();
            team.getTower().spawn();
            team.getBanker().spawn();

            for (UUID playerID : team.getPlayers()) {
                Player player = Bukkit.getPlayer(playerID);
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.teleport(team.getSpawn());
                player.setBedSpawnLocation(team.getSpawn());

                for (ItemStack item : team.getItems()) {
                    player.getInventory().addItem(item);
                }
                team.setArmor(player);
            }
        }

        //GameClock
        Towers.gameClock.start();

        Bukkit.broadcastMessage(Towers.prefix + "§6Das Spiel hat begonnen.");
    }

    private static void loadSpawnerLocations() {
        java.util.Map<String, Object> maps = (java.util.Map<String, Object>) ConfigManager.mapsConfig.getOrDefault("maps", new LinkedTreeMap<>());
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) maps.get(GameManager.map.getName());

        ArrayList<java.util.Map> list = new ArrayList<>();

        if(map.containsKey("spawners"))
            list = (ArrayList<java.util.Map>) map.get("spawners");

        for (java.util.Map<String, Object> linkedTreeMap : list) {
            GameManager.spawnerLocations.add(ConfigManager.getLocationOfMap(linkedTreeMap));
        }
    }

    private static Team getSmallestTeam() {
        int count = Integer.MAX_VALUE;
        Team smallestTeam = null;

        for (Team team : GameManager.teams) {
            if(team.getPlayers().size() < count) {
                count = team.getPlayers().size();
                smallestTeam = team;
            }
        }

        return smallestTeam;
    }

    public static int countVotes(String map) {
        int votes = 0;

        for (Map value : GameManager.votedMap.values()) {
            if(value.getName().equals(map))
                votes++;
        }

        return votes;
    }
}
