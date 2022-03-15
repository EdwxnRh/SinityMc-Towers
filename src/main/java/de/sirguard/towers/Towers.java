package de.sirguard.towers;

import de.sirguard.towers.clock.GameClock;
import de.sirguard.towers.listener.*;
import de.sirguard.towers.utils.ConfigManager;
import de.sirguard.towers.utils.GameManager;
import de.sirguard.towers.commands.ForceStartCommand;
import de.sirguard.towers.commands.SetupCommand;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class Towers extends JavaPlugin {

    public static String prefix = "§7•§8● §6§lTowers §8» §7";
    public static String error = "§c[!] §8» §c";
    public static Plugin plugin;
    public static GameClock gameClock;

    public static Material ziegel = Material.NETHERITE_INGOT;
    public static Material ziegelBlock = Material.NETHERITE_BLOCK;
    public static Material towerBlock = Material.NETHERITE_BLOCK;

    @Override
    public void onEnable() {
        System.out.println("§aBooting up...");
        plugin = this;

        // Config initialization
        ConfigManager.initConfigs();

        if(ConfigManager.mapsConfig.containsKey("lobby")) {
            String worldName = (String) ((Map<String, Object>) ConfigManager.mapsConfig.get("lobby")).get("world");
            if(new File(worldName).exists() && Bukkit.getWorld(worldName) == null) {
                World world = Bukkit.createWorld(new WorldCreator(worldName));
                world.setDifficulty(Difficulty.NORMAL);
                world.setTime(12000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

                for (Entity entity : world.getEntities()) {
                    if(!(entity instanceof Mob) || entity instanceof Player)
                        continue;

                    entity.remove();
                }
            }
        }

        GameManager.init();
        gameClock = new GameClock();


        // Command registration
        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("forcestart").setExecutor(new ForceStartCommand());

        // Listener registration
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new SetupListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PreGameSelectListener(), this);
        Bukkit.getPluginManager().registerEvents(new DropListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeamMapSelectListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPickUpListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new OlliListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpgradesListener(), this);
        Bukkit.getPluginManager().registerEvents(new FireListener(), this);
        Bukkit.getPluginManager().registerEvents(new BankerListener(), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(), this);

        System.out.println("§aStarted successfully.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
