package org.plusmc.pluslib.managing;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.plusmc.pluslib.PlusLib;
import org.plusmc.pluslib.managed.Loadable;
import org.plusmc.pluslib.managed.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register and unregister {@link Tickable} objects.
 */
@SuppressWarnings("unused")
public class TickingManager extends GeneralManager {
    private final Plugin plugin;
    private List<Tickable> TICKABLES;
    private BukkitTask TASK, ASYNC_TASK;
    private long TICK, ASYNC_TICK;
    private int ERRORS;

    protected TickingManager(Plugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    private void tick() {
        for (Tickable tickable : TICKABLES) {
            if (!(tickable.isRunning() && !tickable.isAsync())) continue;
            try {
                tickable.tick(TICK);
            } catch (Exception e) {
                ERRORS++;
                if (ERRORS > 5) {
                    PlusLib.logger().severe("Too many errors in tickables. Stop printing them.");
                } else {
                    PlusLib.logger().warning("Error while ticking " + tickable.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }

        TICK++;
    }

    private void asyncTick() {
        for (Tickable tickable : TICKABLES) {
            if (!(tickable.isRunning() && tickable.isAsync())) continue;
            try {
                tickable.tick(ASYNC_TICK);
            } catch (Exception e) {
                ERRORS++;
                if (ERRORS > 5) {
                    PlusLib.logger().severe("Too many errors in async tickables. Stop printing them.");
                } else {
                    PlusLib.logger().warning("Error while ticking " + tickable.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        ASYNC_TICK++;
    }

    @Override
    public Class<? extends Loadable> getLoadableClass() {
        return Tickable.class;
    }

    @Override
    public void register(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        TICKABLES.add(tickable);
        PlusLib.logger().info("Registered " + tickable.getClass().getSimpleName() + " to the ticking manager.");
    }

    @Override
    public void unregister(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        TICKABLES.add(tickable);
        PlusLib.logger().info("Unregistered " + tickable.getClass().getSimpleName() + " from the ticking manager.");
    }

    @Override
    Plugin getPlugin() {
        return plugin;
    }

    @Override
    protected void init() {
        TICK = 0;
        ASYNC_TICK = 0;
        ERRORS = 0;
        TICKABLES = new ArrayList<>();
        TASK = Bukkit.getScheduler().runTaskTimer(PlusLib.getInstance(), this::tick, 0L, 1L);
        ASYNC_TASK = Bukkit.getScheduler().runTaskTimerAsynchronously(PlusLib.getInstance(), this::asyncTick, 0L, 1L);
    }

    @Override
    protected void shutdown() {
        TASK.cancel();
        ASYNC_TASK.cancel();
        TICKABLES.clear();
    }
}
