package org.plusmc.pluslibcore.util;

public class StringFormatter {

    private StringFormatter() {
        throw new IllegalStateException("Utility class");
    }


    //thanks GitHub copilot :praying_hands:
    // i just realized there is probably something in the standard library that does this
    public static String formatTime(long time) {
        String timeString;
        if (time >= 86400000) {
            timeString = String.format("%d days", time / 86400000);
        } else if (time >= 3600000) {
            timeString = String.format("%dh %02dm %02ds", time / 3600000, (time % 3600000) / 60000, (time % 60000) / 1000);
        } else if (time >= 60000) {
            timeString = String.format("%dm %02ds", time / 60000, (time % 60000) / 1000);
        } else {
            timeString = String.format("%ds", time / 1000);
        }
        return timeString;
    }
}
