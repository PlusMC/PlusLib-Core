package org.plusmc.pluslib.mongo.util;

public class MinecraftUtil {

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

    public static Object getPlayer(String UUID) {
        try {
            if(isBukkit()) {
                Class<?> bukkit = Class.forName("org.bukkit.Bukkit");
                return bukkit.getDeclaredMethod("getPlayer", String.class).invoke(null, UUID);
            } else if (isBungee()) {
                Class<?> bungee = Class.forName("net.md_5.bungee.api.ProxyServer");
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                return bungee.getDeclaredMethod("getPlayer", String.class).invoke(proxy, UUID);
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
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
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
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            Class<?> locationClass = Class.forName("org.bukkit.Location");
            Object location = playerClass.getMethod("getLocation").invoke(player);
            playerClass.getMethod("playSound", locationClass, String.class, float.class, float.class).invoke(player, location, sound, volume, pitch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void kickPlayer(Object player, String message) {
        try {
            if(!isBukkit()) {
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
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
