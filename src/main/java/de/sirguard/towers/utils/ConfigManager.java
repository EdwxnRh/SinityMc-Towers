package de.sirguard.towers.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ConfigManager {

    private static final String MAPS_CONFIG = "plugins/Towers/maps.json";
    private static final String GENERAL_CONFIG = "plugins/Towers/config.json";

    public static Map<String, Object> mapsConfig;
    public static Map<String, Object> generalConfig;

    public static void initConfigs() {
        try {
            Files.createDirectories(new File("plugins/Towers").toPath());
            File maps = new File(MAPS_CONFIG);
            boolean mapCreated = maps.createNewFile();

            File general = new File(GENERAL_CONFIG);
            boolean generalCreated = general.createNewFile();

            if(mapCreated)
                Files.writeString(maps.toPath(), "{}");

            String mapJsonString = Files.readString(maps.toPath());
            mapsConfig = new Gson().<Map<String, Object>>fromJson(mapJsonString, LinkedTreeMap.class);

            if(generalCreated) {
                generalConfig = new LinkedTreeMap<>();
                generalConfig.put("teams", 4.0D);
                generalConfig.put("teamSize", 2.0D);
                Files.writeString(general.toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(generalConfig));
            } else {
                String generalJsonString = Files.readString(general.toPath());
                generalConfig = new Gson().<Map<String, Object>>fromJson(generalJsonString, LinkedTreeMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveMapConfig() {
        File maps = new File(MAPS_CONFIG);
        String mapJsonString = new GsonBuilder().setPrettyPrinting().create().toJson(mapsConfig, LinkedTreeMap.class);
        try {
            Files.writeString(maps.toPath(), mapJsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocationOfMap(Map<String, Object> locationMap) {
        String worldName = (String) locationMap.get("world");
        double x = (double) locationMap.get("x");
        double y = (double) locationMap.get("y");
        double z = (double) locationMap.get("z");
        float yaw = (float) (double) locationMap.get("yaw");
        float pitch = (float) (double) locationMap.get("pitch");

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public static Map<String, Object> getLocationToMap(Location location) {
        return Map.of(
                "world", location.getWorld().getName(),
                "x", location.getBlockX() + 0.5,
                "y", location.getY(),
                "z", location.getBlockZ() + 0.5,
                "yaw", location.getYaw(),
                "pitch", location.getPitch()
        );
    }
}
