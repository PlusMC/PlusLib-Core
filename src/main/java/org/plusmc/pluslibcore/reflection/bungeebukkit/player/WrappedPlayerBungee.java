package org.plusmc.pluslibcore.reflection.bungeebukkit.player;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WrappedPlayerBungee implements WrappedPlayer {
    private final ProxiedPlayer player;


    protected WrappedPlayerBungee(Object player) {
        if (!(player instanceof ProxiedPlayer proxiedPlayer))
            throw new IllegalArgumentException("Player must be a ProxiedPlayer");
        this.player = proxiedPlayer;
    }

    public ProxiedPlayer getPlayerProxied() {
        return player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(new TextComponent(message));
    }

    @Override
    public void kickPlayer(String message) {
        player.disconnect(new TextComponent(message));
    }
}
