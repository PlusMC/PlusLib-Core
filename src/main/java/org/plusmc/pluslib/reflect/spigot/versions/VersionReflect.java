package org.plusmc.pluslib.reflect.spigot.versions;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class VersionReflect {


    public abstract void setCamera(Player player, Entity entity);

    public static VersionReflect getVersionReflect(String version) {
        return switch (version) {
            case "v1_18_R2" -> new v1_18_R2();
            default -> null;
        };
    }


}
