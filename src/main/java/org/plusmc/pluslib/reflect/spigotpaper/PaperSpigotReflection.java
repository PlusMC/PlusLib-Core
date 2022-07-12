package org.plusmc.pluslib.reflect.spigotpaper;

public interface PaperSpigotReflection {


    static boolean isPaper() {
        try {
            // weird way to check if its paper but whatever
            Class.forName("co.aikar.timings.Timing");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
