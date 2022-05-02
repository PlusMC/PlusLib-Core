package org.plusmc.pluslib.mongo.util;

public class OtherUtil {


    //thank you github copilot :praying_hands:
    public static String formatTime(long time) {
        String timeString = "";
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
