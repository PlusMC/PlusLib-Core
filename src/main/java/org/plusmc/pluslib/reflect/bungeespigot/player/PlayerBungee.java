package org.plusmc.pluslib.reflect.bungeespigot.player;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerBungee implements IPlayer {
    private final ProxiedPlayer player;


    protected PlayerBungee(Object player) {
        if(!(player instanceof ProxiedPlayer proxiedPlayer))
            throw new IllegalArgumentException("Player must be a ProxiedPlayer");
        this.player = proxiedPlayer;
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
