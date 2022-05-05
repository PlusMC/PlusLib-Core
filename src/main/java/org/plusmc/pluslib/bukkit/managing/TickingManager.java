package org.plusmc.pluslib.bukkit.managing;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.plusmc.pluslib.bukkit.managed.Loadable;
import org.plusmc.pluslib.bukkit.managed.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register and unregister {@link Tickable} objects.
 */
@SuppressWarnings("unused")
public class TickingManager extends BaseManager {
    private List<Tickable> tickables;
    private BukkitTask tickingTask;
    private BukkitTask asyncTickingTask;
    private long tick;
    private long asyncTick;
    private int errorCount;

    protected TickingManager(JavaPlugin plugin) {
        super(plugin);
    }

    private void tick() {
        for (Tickable tickable : tickables) {
            if (!(tickable.isRunning() && !tickable.isAsync())) continue;
            try {
                tickable.tick(tick);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tick++;
    }

    private void asyncTick() {
        for (Tickable tickable : tickables) {
            if (!(tickable.isRunning() && tickable.isAsync())) continue;
            try {
                tickable.tick(asyncTick);
            } catch (Exception e) {
                errorCount++;
                if (errorCount > 5) {
                    getPlugin().getLogger().severe("Too many errors in async tickables. Stop printing them.");
                } else {
                    getPlugin().getLogger().warning("Error while ticking " + tickable.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
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
        tickables.add(tickable);
        getPlugin().getLogger().info("Registered " + tickable.getClass().getSimpleName() + " to the ticking manager.");
    }

    @Override
    protected void unregister(Loadable loadable) {
        if (!(loadable instanceof Tickable tickable)) return;
        tickables.remove(tickable);
        tickable.unload();
        getPlugin().getLogger().info("Unregistered " + tickable.getClass().getSimpleName() + " from the ticking manager.");
    }

    @Override
    protected void init() {
        tick = 0;
        asyncTick = 0;
        errorCount = 0;
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
