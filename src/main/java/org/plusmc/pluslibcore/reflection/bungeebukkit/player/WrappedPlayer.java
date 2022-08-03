package org.plusmc.pluslibcore.reflection.bungeebukkit.player;

import org.plusmc.pluslibcore.reflection.bungeebukkit.BungeeBukkitReflection;

import java.util.UUID;

public interface WrappedPlayer {

    static WrappedPlayer getPlayer(UUID uuid) {
        if (BungeeBukkitReflection.isBungee()) {
            return new WrappedPlayerBungee(BungeeBukkitReflection.getPlayer(uuid));
        } else if (BungeeBukkitReflection.isBukkit()) {
            return new WrappedPlayerBukkit(BungeeBukkitReflection.getPlayer(uuid));
        }
        throw new IllegalStateException("Unsupported server type");
    }

    void sendMessage(String message);

    void kickPlayer(String message);

    default void playSound(String sound, float volume, float pitch) {
        // empty because bungee doesn't support sounds
    }
}
