package org.plusmc.pluslibcore.reflection.velocitybukkit;



import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class VelocityBukkitReflection {
    public static final String BUKKIT = "org.bukkit.Bukkit";
    public static final String PROXY_SERVER = "com.velocitypowered.api.proxy.ProxyServer";

    private static Object proxy;
    private static Object plugin;
    private static Logger proxyLogger;

    private VelocityBukkitReflection() {
        throw new IllegalStateException("Utility class");
    }


    public static Logger getLogger() {
        try {
            if (isBukkit()) {
                Class<?> bukkitClass = Class.forName(BUKKIT);

                Object pluginManager = bukkitClass.getMethod("getPluginManager").invoke(null);
                Object pluginInstance = pluginManager.getClass().getMethod("getPlugin", String.class).invoke(pluginManager, "PlusLib");
                if (pluginInstance == null)
                    return null;
                Object logger = pluginInstance.getClass().getMethod("getLogger").invoke(pluginInstance);
                return (Logger) logger;
            } else if (isVelocity()) {
                return proxyLogger;
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

    public static boolean isVelocity() {
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
            } else if (isVelocity()) {
                Class<?> velocity = Class.forName(PROXY_SERVER);
                return velocity.getDeclaredMethod("getPlayer", UUID.class).invoke(proxy, uuid);
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
            } else if (isVelocity()) {
                //TODO: check if this actually runs async also a thread would be better for everything here LOL
                Class<?> velocity = Class.forName(PROXY_SERVER);
                Class<?> schedulerClass = Class.forName("com.velocitypowered.api.scheduler.Scheduler");
                Class<?> taskBuilderClass = Class.forName("com.velocitypowered.api.scheduler.Scheduler$TaskBuilder");

                Object scheduler = velocity.getMethod("getScheduler").invoke(proxy);
                Object taskBuilder = schedulerClass.getMethod("buildTask", Object.class, Runnable.class).invoke(scheduler, plugin, runnable);
                taskBuilderClass.getMethod("schedule").invoke(taskBuilder);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initProxy(Object proxy, Logger logger, Object plugin) {
        VelocityBukkitReflection.proxy = proxy;
        VelocityBukkitReflection.proxyLogger = logger;
        VelocityBukkitReflection.plugin = plugin;
    }

}
