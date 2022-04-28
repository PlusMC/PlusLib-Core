package org.plusmc.pluslib.managed;

public interface Loadable {
    default void load() {};
    default void unload() {};
}
