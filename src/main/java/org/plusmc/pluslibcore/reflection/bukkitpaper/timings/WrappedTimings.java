package org.plusmc.pluslibcore.reflection.bukkitpaper.timings;

import org.bukkit.plugin.Plugin;
import org.plusmc.pluslibcore.reflection.bukkitpaper.BukkitPaperReflection;

public interface WrappedTimings {
    static WrappedTimings create(Plugin plugin, String name) {
        if (BukkitPaperReflection.isPaper()) {
            return new WrappedTimingsPaper(plugin, name);
        } else {
            return new WrappedTimingsBukkit(name);
        }
    }

    void startTiming();

    void stopTiming();

    default void close() {
    }
}
