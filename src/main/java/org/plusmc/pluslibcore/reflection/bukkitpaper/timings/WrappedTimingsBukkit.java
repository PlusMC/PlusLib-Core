package org.plusmc.pluslibcore.reflection.bukkitpaper.timings;

import org.spigotmc.CustomTimingsHandler;

public class WrappedTimingsBukkit implements WrappedTimings {
    CustomTimingsHandler handler;

    public WrappedTimingsBukkit(String name) {
        this.handler = new CustomTimingsHandler(name);
    }

    @Override
    public void startTiming() {
        handler.startTiming();
    }

    @Override
    public void stopTiming() {
        handler.stopTiming();
    }
}
