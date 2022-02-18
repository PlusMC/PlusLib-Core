package org.plusmc.pluslib.managing;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.plusmc.pluslib.managed.Loadable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Registers all the commands, items, and tickables to their respectable managers.
 */
@SuppressWarnings("unused")
public abstract class GeneralManager {
    private static final List<GeneralManager> MANAGERS = new ArrayList<>();

    protected GeneralManager(Plugin plugin) {
        MANAGERS.add(this);
    }

    @Nullable
    public static <T extends GeneralManager> T createManager(Class<T> manager, Plugin plugin) {
        T obj = GeneralManager.getManager(plugin, manager);
        if (obj != null)
            return obj;

        try {
            Constructor<T> constructor = manager.getConstructor(Plugin.class);
            obj = constructor.newInstance(plugin);
        } catch (Exception e) {
            return null;
        }

        return obj;
    }

    /**
     * Registers all the objects to their respectable manager.
     *
     * @param object The object to register.
     */
    public static void registerAny(Loadable object, Plugin plugin) {
        for (GeneralManager manager : MANAGERS)
            if (manager.getPlugin().equals(plugin))
                if (manager.getLoadableClass().equals(object.getClass()))
                    manager.register(object);

    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends GeneralManager> T getManager(Plugin plugin, Class<T> manager) {
        for (GeneralManager m : MANAGERS)
            if (m.getClass().equals(manager) && m.getPlugin().equals(plugin))
                return (T) m;

        return null;
    }

    public static List<GeneralManager> getAllManagers() {
        return new ArrayList<>(MANAGERS);
    }

    public static void shutdownAll(Plugin plugin) {
        for (GeneralManager manager : MANAGERS)
            if (manager.getPlugin().equals(plugin))
                manager.shutdown();
    }

    abstract Class<? extends Loadable> getLoadableClass();

    abstract void register(Loadable loadable);

    abstract void unregister(Loadable loadable);

    abstract Plugin getPlugin();

    abstract protected void init();

    abstract protected void shutdown();
}
