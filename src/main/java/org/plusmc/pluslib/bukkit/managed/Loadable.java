package org.plusmc.pluslib.bukkit.managed;

public interface Loadable {
    default void load() {}
    default void unload() {}
}
