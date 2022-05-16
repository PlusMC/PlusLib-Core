package org.plusmc.pluslib.bukkit.managing;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.plusmc.pluslib.bukkit.managed.Loadable;
import org.plusmc.pluslib.bukkit.managed.Tickable;
import org.plusmc.pluslib.reflection.timings.ITimings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a way to register and unregister {@link Tickable} objects.
 */
@SuppressWarnings("unused")
public class TickingManager extends BaseManager {
    private List<Map.Entry<Tickable, ITimings>> tickables;
    private BukkitTask tickingTask;
    private BukkitTask asyncTickingTask;
    private long tick;
    private long asyncTick;

    protected TickingManager(JavaPlugin plugin) {
        super(plugin);
    }

    private void tick() {
        for (Map.Entry<Tickable, ITimings> entry : tickables) {
            Tickable tickable = entry.getKey();
            ITimings timings = entry.getValue();
            if (!(tickable.isRunning() && !tickable.isAsync())) continue;
            timings.startTiming();
            try {
                tickable.tick(tick);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timings.stopTiming();
        }

        tick++;
    }

    private void asyncTick() {
        for (Map.Entry<Tickable, ITimings> entry : tickables) {
            Tickable tickable = entry.getKey();
            ITimings timings = entry.getValue();
            if (!(tickable.isRunning() && tickable.isAsync())) continue;
            timings.startTiming();
            try {
                tickable.tick(asyncTick);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timings.stopTiming();
        }
        asyncTick++;
    }

    @Override
    public Class<? extends Loadable> getManaged() {
        return Tickable.class;
    }

    @Override
    protected void register(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        tickables.add(new HashMap.SimpleEntry<>(tickable, ITimings.create(getPlugin(), tickable.getClass().getSimpleName() + " (tickable)")));
        getPlugin().getLogger().info("Registered " + tickable.getClass().getSimpleName() + " to the ticking manager.");
    }

    @Override
    protected void unregister(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        tickables.removeIf(entry -> entry.getKey() == tickable);
        tickable.unload();
        getPlugin().getLogger().info("Unregistered " + tickable.getClass().getSimpleName() + " from the ticking manager.");
    }

    @Override
    protected void init() {
        tick = 0;
        asyncTick = 0;
        tickables = new ArrayList<>();
        tickingTask = Bukkit.getScheduler().runTaskTimer(getPlugin(), this::tick, 0L, 1L);
        asyncTickingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), this::asyncTick, 0L, 1L);
    }

    @Override
    protected void shutdown() {
        tickingTask.cancel();
        asyncTickingTask.cancel();
        tickables.clear();
    }
}
