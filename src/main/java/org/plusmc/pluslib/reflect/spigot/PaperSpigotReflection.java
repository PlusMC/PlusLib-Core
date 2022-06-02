package org.plusmc.pluslib.reflect.spigot;

import org.bukkit.Bukkit;

public abstract class PaperSpigotReflection {


    public static boolean isPaper() {
        try {
            // weird way to check if its paper but whatever
            Class.forName("co.aikar.timings.Timing");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
