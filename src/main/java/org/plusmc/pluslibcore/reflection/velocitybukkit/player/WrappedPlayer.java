package org.plusmc.pluslibcore.reflection.velocitybukkit.player;

import org.plusmc.pluslibcore.reflection.velocitybukkit.VelocityBukkitReflection;

import java.util.UUID;

public interface WrappedPlayer {

    static WrappedPlayer getPlayer(UUID uuid) {
        if (VelocityBukkitReflection.isVelocity()) {
            return new WrappedPlayerVelocity(VelocityBukkitReflection.getPlayer(uuid));
        } else if (VelocityBukkitReflection.isBukkit()) {
            return new WrappedPlayerBukkit(VelocityBukkitReflection.getPlayer(uuid));
        }
        throw new IllegalStateException("Unsupported server type");
    }

    void sendMessage(String message);

    void kickPlayer(String message);

    default void playSound(String sound, float volume, float pitch) {
        // empty because bungee doesn't support sounds
    }
}
