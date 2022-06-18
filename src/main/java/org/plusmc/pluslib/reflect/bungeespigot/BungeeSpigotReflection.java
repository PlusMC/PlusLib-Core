package org.plusmc.pluslib.reflect.bungeespigot;


import org.plusmc.pluslib.bukkit.PlusLibBukkit;
import org.plusmc.pluslib.bungee.PlusLibBungee;

import java.util.UUID;
import java.util.logging.Logger;

public abstract class BungeeSpigotReflection {
    public static final String BUKKIT = "org.bukkit.Bukkit";
    public static final String PROXY_SERVER = "net.md_5.bungee.api.ProxyServer";

    private BungeeSpigotReflection() {
        throw new IllegalStateException("Utility class");
    }


    public static Logger getLogger() {
        if (isBukkit()) {
            return PlusLibBukkit.logger();
        } else if (isBungee()) {
            return PlusLibBungee.logger();
        }
        return null;
    }

    public static boolean isBukkit() {
        try {
            Class.forName(BUKKIT);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isBungee() {
        try {
            Class.forName(PROXY_SERVER);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Object getPlayer(UUID uuid) {
        try {
            if (isBukkit()) {
                Class<?> bukkit = Class.forName(BUKKIT);
                return bukkit.getDeclaredMethod("getPlayer", UUID.class).invoke(null, uuid);
            } else if (isBungee()) {
                Class<?> bungee = Class.forName(PROXY_SERVER);
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                return bungee.getDeclaredMethod("getPlayer", UUID.class).invoke(proxy, uuid);
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void runAsync(Runnable runnable) {
        try {
            if (isBukkit()) {
                Class<?> bukkitClass = Class.forName(BUKKIT);
                Class<?> schedulerClass = Class.forName("org.bukkit.scheduler.BukkitScheduler");
                Class<?> pluginClass = Class.forName("org.bukkit.plugin.Plugin");
                Object scheduler = bukkitClass.getMethod("getScheduler").invoke(null);
                schedulerClass.getMethod("runTaskAsynchronously", pluginClass, Runnable.class).invoke(scheduler, PlusLibBukkit.getInstance(), runnable);
            } else if (isBungee()) {
                Class<?> bungee = Class.forName(PROXY_SERVER);
                Class<?> schedulerClass = Class.forName("net.md_5.bungee.api.scheduler.TaskScheduler");
                Class<?> pluginClass = Class.forName("net.md_5.bungee.api.plugin.Plugin");
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                Object scheduler = bungee.getDeclaredMethod("getScheduler").invoke(proxy);
                schedulerClass.getMethod("runAsync", pluginClass, Runnable.class).invoke(scheduler, PlusLibBungee.getInstance(), runnable);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
