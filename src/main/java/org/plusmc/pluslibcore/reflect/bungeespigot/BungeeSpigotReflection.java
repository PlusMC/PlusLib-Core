package org.plusmc.pluslibcore.reflect.bungeespigot;



import java.util.UUID;
import java.util.logging.Logger;

public abstract class BungeeSpigotReflection {
    public static final String BUKKIT = "org.bukkit.Bukkit";
    public static final String PROXY_SERVER = "net.md_5.bungee.api.ProxyServer";

    private BungeeSpigotReflection() {
        throw new IllegalStateException("Utility class");
    }


    public static Logger getLogger() {
        try {
            if (isBukkit()) {
                Class<?> bukkitClass = Class.forName(BUKKIT);


                Object pluginManager = bukkitClass.getMethod("getPluginManager").invoke(null);
                Object pluginInstance = pluginManager.getClass().getMethod("getPlugin", String.class).invoke(pluginManager, "PlusLib");
                Object logger = pluginInstance.getClass().getMethod("getLogger").invoke(pluginInstance);
                return (Logger) logger;
            } else if (isBungee()) {
                Class<?> bungee = Class.forName(PROXY_SERVER);
                Class<?> pluginClass = Class.forName("net.md_5.bungee.api.plugin.Plugin");
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                Object pluginManager = bungee.getDeclaredMethod("getPluginManager").invoke(proxy);

                Object pluginInstance = pluginManager.getClass().getDeclaredMethod("getPlugin", String.class).invoke(pluginManager, "PlusLib");
                Object logger = pluginInstance.getClass().getMethod("getLogger").invoke(pluginInstance);
                return (Logger) logger;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                Object pluginManager = bukkitClass.getMethod("getPluginManager").invoke(null);
                Object pluginInstance = pluginManager.getClass().getMethod("getPlugin", String.class).invoke(pluginManager, "PlusLib");


                schedulerClass.getMethod("runTaskAsynchronously", pluginClass, Runnable.class).invoke(scheduler, pluginInstance, runnable);
            } else if (isBungee()) {
                Class<?> bungee = Class.forName(PROXY_SERVER);
                Class<?> schedulerClass = Class.forName("net.md_5.bungee.api.scheduler.TaskScheduler");
                Class<?> pluginClass = Class.forName("net.md_5.bungee.api.plugin.Plugin");
                Object proxy = bungee.getDeclaredMethod("getInstance").invoke(null);
                Object pluginManager = bungee.getDeclaredMethod("getPluginManager").invoke(proxy);
                Object pluginInstance = pluginManager.getClass().getDeclaredMethod("getPlugin", String.class).invoke(pluginManager, "PlusLib");
                Object scheduler = bungee.getDeclaredMethod("getScheduler").invoke(proxy);
                schedulerClass.getMethod("runAsync", pluginClass, Runnable.class).invoke(scheduler, pluginInstance, runnable);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
