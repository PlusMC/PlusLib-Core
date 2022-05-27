package org.plusmc.pluslib.reflect.spigot;

import org.bukkit.Bukkit;
import org.plusmc.pluslib.reflect.spigot.versions.VersionReflect;

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

    public static String getBukkitVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }


    public static VersionReflect getVersionReflect() {
        return VersionReflect.getVersionReflect(getBukkitVersion());
    }

}
