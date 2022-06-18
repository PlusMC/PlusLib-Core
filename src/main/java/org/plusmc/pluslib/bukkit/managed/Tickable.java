package org.plusmc.pluslib.bukkit.managed;

import org.plusmc.pluslib.bukkit.managing.TickingManager;

/**
 * A class has a tick method that is called every tick.
 * Useful for anything that requires stuff to run on every tick.
 */
@SuppressWarnings("unused")
public interface Tickable extends Loadable {


    /**
     * The {@link TickingManager} will call this method for every tick.
     *
     * @param tick The amount of ticks since the {@link TickingManager} was started.
     */

    void tick(long tick);

    /**
     * @return true if the {@link Tickable} should be run asynchronously.
     */
    default boolean isAsync() {
        return false;
    }

    /**
     * @return true if the {@link Tickable} should run next tick.
     */
    default boolean isRunning() {
        return true;
    }

}
