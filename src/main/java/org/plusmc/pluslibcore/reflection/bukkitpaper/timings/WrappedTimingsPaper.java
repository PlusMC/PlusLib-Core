package org.plusmc.pluslibcore.reflection.bukkitpaper.timings;


import org.bukkit.plugin.Plugin;

public class WrappedTimingsPaper implements WrappedTimings {
    private final co.aikar.timings.Timing timings;

    public WrappedTimingsPaper(Plugin plugin, String name) {
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

