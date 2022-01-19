package org.plusmc.pluslib;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.plusmc.pluslib.tickable.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register and unregister {@link Tickable} objects.
 */
@SuppressWarnings("unused")
public class TickingManager {
    private static List<Tickable> TICKABLES;
    private static BukkitTask TASK;
    private static long TICK;
    private static BukkitTask ASYNC_TASK;
    private static long ASYNC_TICK;

    private static void tick() {
        for (Tickable tickable : TICKABLES) {
            if (tickable.isRunning() && !tickable.isAsync())
                tickable.tick(TICK);
        }
        TICK++;
    }

    private static void asyncTick() {
        for (Tickable tickable : TICKABLES) {
            if (tickable.isRunning() && tickable.isAsync())
                tickable.tick(ASYNC_TICK);
        }
        ASYNC_TICK++;
    }

    /**
     * Registers a tickable to the ticking manager.
     *
     * @param tickable The tickable to register.
     */
    public static void register(Tickable tickable) {
        TICKABLES.add(tickable);
        PlusLib.logger().info("Registered " + tickable.getClass().getSimpleName() + " to the ticking manager.");
    }

    /**
     * Unregisters a tickable from the ticking manager.
     *
     * @param tickable The tickable to unregister.
     */
    public static void unregister(Tickable tickable) {
        TICKABLES.add(tickable);
        PlusLib.logger().info("Unregistered " + tickable.getClass().getSimpleName() + " from the ticking manager.");
    }

    static void start() {
        TICK = 0;
        ASYNC_TICK = 0;
        TICKABLES = new ArrayList<>();
        TASK = Bukkit.getScheduler().runTaskTimer(PlusLib.getInstance(), TickingManager::tick, 0L, 1L);
        ASYNC_TASK = Bukkit.getScheduler().runTaskTimerAsynchronously(PlusLib.getInstance(), TickingManager::asyncTick, 0L, 1L);
    }

    static void stop() {
        TASK.cancel();
        ASYNC_TASK.cancel();
        TICKABLES.clear();
    }
}
