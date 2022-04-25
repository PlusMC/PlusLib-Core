package org.plusmc.pluslib.managing;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.plusmc.pluslib.managed.Loadable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Registers all the commands, items, and tickables to their respectable managers.
 */
@SuppressWarnings("unused")
public abstract class BaseManager {
    private static final List<BaseManager> MANAGERS = new ArrayList<>();

    private final JavaPlugin plugin;
    protected BaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        MANAGERS.add(this);
    }

    @Nullable
    public static <T extends BaseManager> T createManager(Class<T> manager, JavaPlugin plugin) {
        T obj = BaseManager.getManager(plugin, manager);
        if (obj != null)
            return obj;

        try {
            Constructor<T> constructor = manager.getDeclaredConstructor(JavaPlugin.class);
            constructor.setAccessible(true);
            obj = constructor.newInstance(plugin);
            obj.init();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return obj;
    }

    /**
     * Registers all the objects to their respectable manager.
     *
     * @param object The object to register.
     */
    public static void registerAny(Loadable object, JavaPlugin plugin) {
        for (BaseManager manager : MANAGERS)
            if (manager.getPlugin().equals(plugin))
                if (manager.getManaged().equals(object.getClass()))
                    manager.register(object);

    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends BaseManager> T getManager(JavaPlugin plugin, Class<T> manager) {
        for (BaseManager m : MANAGERS)
            if (m.getClass().equals(manager) && m.getPlugin().equals(plugin))
                return (T) m;

        return null;
    }

    public static List<BaseManager> getAllManagers() {
        return new ArrayList<>(MANAGERS);
    }

    public static void shutdownAll(JavaPlugin plugin) {
        for (BaseManager manager : MANAGERS)
            if (manager.getPlugin().equals(plugin))
                manager.shutdown();
    }

    abstract Class<? extends Loadable> getManaged();

    abstract void register(Loadable loadable);

    abstract void unregister(Loadable loadable);

    JavaPlugin getPlugin() {
        return plugin;
    }

    abstract protected void init();

    abstract protected void shutdown();
}
