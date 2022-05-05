package org.plusmc.pluslib.mongo.util;

import java.util.UUID;

public class MinecraftUtil {

    private MinecraftUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BUKKIT_ENTITY_PLAYER = "org.bukkit.entity.Player";

    public static boolean isBukkit() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isBungee() {
        try {
            Class.forName("net.md_5.bungee.api.ProxyServer");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Object getPlayer(String uuid) {
        try {
            if(isBukkit()) {
                Class<?> bukkit = Class.forName("org.bukkit.Bukkit");
                return bukkit.getDeclaredMethod("getPlayer", UUID.class).invoke(null, UUID.fromString(uuid));
            } else if (isBungee()) {
                Class<?> bungee = Class.forName("net.md_5.bungee.api.ProxyServer");
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                return bungee.getDeclaredMethod("getPlayer", UUID.class).invoke(proxy, UUID.fromString(uuid));
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void sendMessage(Object player, String message) {
        try {
            if(isBukkit()) {
                Class<?> playerClass = Class.forName(BUKKIT_ENTITY_PLAYER);
                playerClass.getMethod("sendMessage", String.class).invoke(player, message);
            } else if(isBungee()) {
                Class<?> playerClass = Class.forName("net.md_5.bungee.api.connection.ProxiedPlayer");
                playerClass.getMethod("sendMessage", String.class).invoke(player,message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void playSound(Object player, String sound, float volume, float pitch) {
        try {
            if(!isBukkit()) return;
            Class<?> playerClass = Class.forName(BUKKIT_ENTITY_PLAYER);
            Class<?> locationClass = Class.forName("org.bukkit.Location");
            Object location = playerClass.getMethod("getLocation").invoke(player);
            playerClass.getMethod("playSound", locationClass, String.class, float.class, float.class).invoke(player, location, sound, volume, pitch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void kickPlayer(Object player, String message) {
        try {
            if(isBukkit()) {
                Class<?> playerClass = Class.forName(BUKKIT_ENTITY_PLAYER);
                playerClass.getMethod("kickPlayer", String.class).invoke(player, message);
            } else if (isBungee()) {
                Class<?> playerClass = Class.forName("net.md_5.bungee.api.connection.ProxiedPlayer");
                playerClass.getMethod("disconnect", String.class).invoke(player, message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
