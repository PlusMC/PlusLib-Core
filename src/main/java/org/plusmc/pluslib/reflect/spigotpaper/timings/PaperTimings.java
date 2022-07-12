package org.plusmc.pluslib.reflect.spigotpaper.timings;


import org.bukkit.plugin.Plugin;

public class PaperTimings implements ITimings {
    private final co.aikar.timings.Timing timings;

    public PaperTimings(Plugin plugin, String name) {
        timings = co.aikar.timings.Timings.of(plugin, name);
    }


    @Override
    public void startTiming() {
        timings.startTiming();
    }

    @Override
    public void stopTiming() {
        timings.stopTiming();
    }

    @Override
    public void close() {
        timings.close();
    }
}

