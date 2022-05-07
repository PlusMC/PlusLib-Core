package org.plusmc.pluslib.bungee;


import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.plusmc.pluslib.mongo.DatabaseHandler;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlusLibBungee extends Plugin {
    private static PlusLibBungee instance;
    private ScheduledTask task;

    @Override
    public void onEnable() {
        setInstance(this);
        TaskScheduler scheduler = ProxyServer.getInstance().getScheduler();
        DatabaseHandler.createInstance();
        this.task = scheduler.schedule(this, () -> {
            if(DatabaseHandler.getInstance() != null && DatabaseHandler.getInstance().isLoaded())
                DatabaseHandler.getInstance().updateCache();
        }, 30, 30, TimeUnit.SECONDS);
    }

    public static Logger logger()  {
        return instance.getLogger();
    }

    private static void setInstance(PlusLibBungee instance) {
        PlusLibBungee.instance = instance;
    }

    @Override
    public void onDisable() {
        task.cancel();
        setInstance(null);
    }

    public static PlusLibBungee getInstance() {
        return instance;
    }
}
