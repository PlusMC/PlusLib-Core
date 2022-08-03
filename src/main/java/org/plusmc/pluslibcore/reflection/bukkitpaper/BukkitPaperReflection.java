package org.plusmc.pluslibcore.reflection.bukkitpaper;

public interface BukkitPaperReflection {


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
