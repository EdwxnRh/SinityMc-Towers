package de.sirguard.towers.enums;

import org.bukkit.Color;
import org.bukkit.Material;

public enum Teams {

    // SORTED LIST FOR STANDARD TEAM COLOR
    RED("Rot", "c", Color.RED, Material.RED_CANDLE),
    GREEN("Grün", "a", Color.LIME, Material.LIME_CANDLE),
    YELLOW("Gelb", "e", Color.YELLOW, Material.YELLOW_CANDLE),
    BLUE("Blau", "9", Color.BLUE, Material.BLUE_CANDLE),

    AQUA("Aqua", "b", Color.AQUA, Material.LIGHT_BLUE_CANDLE),
    LIGHT_PURPLE("Pink", "d", Color.FUCHSIA, Material.PINK_CANDLE),
    GOLD("Orange", "6", Color.ORANGE, Material.ORANGE_CANDLE),
    DARK_AQUA("Türkis", "3", Color.TEAL, Material.CYAN_CANDLE),

    DARK_PURPLE("Lila", "5", Color.PURPLE, Material.PURPLE_CANDLE),
    DARK_GREEN("Dunkelgrün", "2", Color.GREEN, Material.GREEN_CANDLE),

    WHITE("Weiß", "f", Color.WHITE, Material.WHITE_CANDLE),
    BLACK("Schwarz", "0", Color.BLACK, Material.BLACK_CANDLE);

    /*
    DARK_RED("Dunkelrot", "4", Color.MAROON),
    DARK_BLUE("Dunkelblau", "1", Color.NAVY),

    GRAY("Grau", "7", Color.SILVER),
    DARK_GRAY("Dunkelgrau", "8", Color.GRAY),

    */


    private final String name;
    private final String color;
    private final Color leatherColor;
    private final Material candleColor;

    Teams(String name, String color, Color leatherColor, Material cancleColor) {
        this.name = name;
        this.color = color;
        this.leatherColor = leatherColor;
        this.candleColor = cancleColor;
    }

    public static boolean isPossible(String teamName) {
        for (Teams value : Teams.values()) {
            if (value.name().equals(teamName))
                return true;
        }
        return false;
    }

    public static Teams getTeamByEnumName(String teamName) {
        for (Teams value : Teams.values()) {
            if (value.name().equals(teamName))
                return value;
        }
        return Teams.BLACK;
    }

    public static Teams getTeamByDisplayName(String teamDisplayName) {
        for (Teams value : Teams.values()) {
            if (value.getName().equals(teamDisplayName))
                return value;
        }
        return Teams.BLACK;
    }

    public static String getColorOfTeam(String teamName) {
        for (Teams value : Teams.values()) {
            if (value.name().equals(teamName))
                return value.getColor();
        }
        return "7";
    }

    public String getTitle() {
        return "§" + color + name;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Color getLeatherColor() {
        return leatherColor;
    }

    public Material getCandle() {
        return candleColor;
    }
}
