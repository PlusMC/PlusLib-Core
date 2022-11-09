package org.plusmc.pluslibcore.reflection.velocitybukkit.player;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class WrappedPlayerVelocity implements WrappedPlayer {
    private final Player player;


    protected WrappedPlayerVelocity(Object player) {
        if (!(player instanceof Player proxiedPlayer))
            throw new IllegalArgumentException("Player must be a ProxiedPlayer");
        this.player = proxiedPlayer;
    }

    public Player getPlayerProxied() {
        return player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Component.text(message));
    }

    @Override
    public void kickPlayer(String message) {
        player.disconnect(Component.text(message));
    }
}
