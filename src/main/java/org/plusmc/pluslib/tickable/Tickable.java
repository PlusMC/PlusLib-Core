package org.plusmc.pluslib.tickable;

import org.plusmc.pluslib.TickingManager;

/**
 * A class has a tick method that is called every tick.
 * Useful for anything that requires stuff to run on every tick.
 */
@SuppressWarnings("unused")
public interface Tickable {
    /**
     * Called when {@link Tickable} is added to {@link TickingManager}.
     */
    default void start() {
    }

    /**
     * Called when {@link Tickable} is removed from {@link TickingManager}.
     */
    default void stop() {
    }

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
