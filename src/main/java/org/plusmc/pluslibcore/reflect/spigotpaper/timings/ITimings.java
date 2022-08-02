package org.plusmc.pluslibcore.reflect.spigotpaper.timings;

import org.bukkit.plugin.Plugin;
import org.plusmc.pluslibcore.reflect.spigotpaper.PaperSpigotReflection;

public interface ITimings {
    static ITimings create(Plugin plugin, String name) {
        if (PaperSpigotReflection.isPaper()) {
            return new PaperTimings(plugin, name);
        } else {
            return new SpigotTimings(name);
        }
    }

    void startTiming();

    void stopTiming();

    default void close() {
    }
}
