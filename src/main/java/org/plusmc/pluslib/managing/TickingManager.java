package org.plusmc.pluslib.managing;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.plusmc.pluslib.managed.Loadable;
import org.plusmc.pluslib.managed.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register and unregister {@link Tickable} objects.
 */
@SuppressWarnings("unused")
public class TickingManager extends BaseManager {
    private List<Tickable> Tickables;
    private BukkitTask TickingTask, AsyncTickingTask;
    private long TICK, ASYNC_TICK;
    private int errorCount;

    protected TickingManager(Plugin plugin) {
        super(plugin);
    }

    private void tick() {
        for (Tickable tickable : Tickables) {
            if (!(tickable.isRunning() && !tickable.isAsync())) continue;
            try {
                tickable.tick(TICK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TICK++;
    }

    private void asyncTick() {
        for (Tickable tickable : Tickables) {
            if (!(tickable.isRunning() && tickable.isAsync())) continue;
            try {
                tickable.tick(ASYNC_TICK);
            } catch (Exception e) {
                errorCount++;
                if (errorCount > 5) {
                    getPlugin().getLogger().severe("Too many errors in async tickables. Stop printing them.");
                } else {
                    getPlugin().getLogger().warning("Error while ticking " + tickable.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        ASYNC_TICK++;
    }

    @Override
    public Class<? extends Loadable> getManaged() {
        return Tickable.class;
    }

    @Override
    public void register(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        Tickables.add(tickable);
        getPlugin().getLogger().info("Registered " + tickable.getClass().getSimpleName() + " to the ticking manager.");
    }

    @Override
    public void unregister(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        Tickables.add(tickable);
        getPlugin().getLogger().info("Unregistered " + tickable.getClass().getSimpleName() + " from the ticking manager.");
    }

    @Override
    protected void init() {
        TICK = 0;
        ASYNC_TICK = 0;
        errorCount = 0;
        Tickables = new ArrayList<>();
        TickingTask = Bukkit.getScheduler().runTaskTimer(getPlugin(), this::tick, 0L, 1L);
        AsyncTickingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), this::asyncTick, 0L, 1L);
    }

    @Override
    protected void shutdown() {
        TickingTask.cancel();
        AsyncTickingTask.cancel();
        Tickables.clear();
    }
}
