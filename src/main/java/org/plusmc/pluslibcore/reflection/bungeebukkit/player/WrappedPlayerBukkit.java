package org.plusmc.pluslibcore.reflection.bungeebukkit.player;

import org.bukkit.entity.Player;

public class WrappedPlayerBukkit implements WrappedPlayer {

    private final Player player;

    protected WrappedPlayerBukkit(Object player) {
        if (!(player instanceof Player p))
            throw new IllegalArgumentException("Player is not a Player");
        this.player = p;
    }
    public Player getPlayerBukkit() {
        return player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void kickPlayer(String message) {
        player.kickPlayer(message);
    }

    @Override
    public void playSound(String sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
