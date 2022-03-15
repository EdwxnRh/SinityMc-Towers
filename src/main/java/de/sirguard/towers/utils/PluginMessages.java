package de.sirguard.towers.utils;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class PluginMessages {
    public static Component getSetupMessage(){
        return Component.text()
                .append(Component.text("\n"))
                .append(Component.text("Informationen über das Towers Setup").color(NamedTextColor.GOLD))
                .append(Component.text("\n"))
                .append(Component.text("\n"))

                .append(Component.text("╰ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Nutze ").color(NamedTextColor.GRAY))
                .append(Component.text( "/setup lobby ").color(NamedTextColor.GOLD))
                .append(Component.text("um die Lobby zu setzen").color(NamedTextColor.GRAY))
                .append(Component.text("\n"))

                .append(Component.text("╰ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Nutze ").color(NamedTextColor.GRAY))
                .append(Component.text( "/setup map <BUILDER> <SIZE> ").color(NamedTextColor.GOLD))
                .append(Component.text("um eine Map einzurichten").color(NamedTextColor.GRAY))
                .append(Component.text("\n"))

                .append(Component.text("╰ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Nutze ").color(NamedTextColor.GRAY))
                .append(Component.text( "/setup team <COLOR> ").color(NamedTextColor.GOLD))
                .append(Component.text("um ein Team einzurichten").color(NamedTextColor.GRAY))
                .append(Component.text("\n"))

                .append(Component.text("╰ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Nutze ").color(NamedTextColor.GRAY))
                .append(Component.text( "/setup spawner ").color(NamedTextColor.GOLD))
                .append(Component.text("um einen Spawner zu setzen").color(NamedTextColor.GRAY))
                .append(Component.text("\n"))

                .append(Component.text("╰ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Type ").color(NamedTextColor.GRAY))
                .append(Component.text( "/setup tp <MAP> ").color(NamedTextColor.GOLD))
                .append(Component.text("um dich auf eine Map zu teleportieren").color(NamedTextColor.GRAY))
                .append(Component.text("\n"))

                .build();
    }
}
