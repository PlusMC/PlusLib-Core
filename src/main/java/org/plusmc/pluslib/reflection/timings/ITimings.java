package org.plusmc.pluslib.reflection.timings;

import org.bukkit.plugin.Plugin;
import org.plusmc.pluslib.reflection.PaperSpigotReflection;

public interface ITimings {
    static ITimings create(Plugin plugin, String name) {
        if(PaperSpigotReflection.isPaper()) {
            return new PaperTimings(plugin, name);
        } else {
            return new SpigotTimings(name);
        }
    }

    void startTiming();
    void stopTiming();
    default void close() {};
}
