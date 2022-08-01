package org.plusmc.pluslib.bukkit.managed;

public interface Loadable {
    default void load() {
    }

    default String getName() {
        return getClass().getSimpleName() + this.hashCode();
    }

    default void unload() {
    }
}
