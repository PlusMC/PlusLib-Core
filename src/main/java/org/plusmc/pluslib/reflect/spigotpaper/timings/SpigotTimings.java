package org.plusmc.pluslib.reflect.spigotpaper.timings;

import org.spigotmc.CustomTimingsHandler;

public class SpigotTimings implements ITimings {
    CustomTimingsHandler handler;

    public SpigotTimings(String name) {
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
