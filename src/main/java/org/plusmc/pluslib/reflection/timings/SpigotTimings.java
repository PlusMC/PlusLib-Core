package org.plusmc.pluslib.reflection.timings;

import org.spigotmc.CustomTimingsHandler;

public class SpigotTimings implements Timings {
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
