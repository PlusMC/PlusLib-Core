package org.plusmc.pluslib.reflection;

public class PaperSpigotReflection {

    public static boolean isPaper() {
        try {
            Class.forName("co.aikar.timings.Timing");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
