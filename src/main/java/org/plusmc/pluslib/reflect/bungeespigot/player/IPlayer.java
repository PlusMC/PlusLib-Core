package org.plusmc.pluslib.reflect.bungeespigot.player;

import org.plusmc.pluslib.reflect.bungeespigot.BungeeSpigotReflection;

import java.util.UUID;

public interface IPlayer {

    static IPlayer getPlayer(UUID uuid) {
        if (BungeeSpigotReflection.isBungee()) {
            return new PlayerBungee(BungeeSpigotReflection.getPlayer(uuid));
        } else if (BungeeSpigotReflection.isBukkit()) {
            return new PlayerSpigot(BungeeSpigotReflection.getPlayer(uuid));
        }
        throw new IllegalStateException("Unsupported server type");
    }

    void sendMessage(String message);

    void kickPlayer(String message);

    default void playSound(String sound, float volume, float pitch) {
        // empty because bungee doesn't support sounds
    }
}
