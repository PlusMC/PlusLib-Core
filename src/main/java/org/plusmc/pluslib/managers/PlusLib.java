package org.plusmc.pluslib.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.util.BukkitUtil;
import org.plusmc.pluslib.util.BungeeUtil;

import java.util.logging.Logger;


/**
 * The plugin class for the PlusLib
 */
@SuppressWarnings("unused")
public final class PlusLib extends JavaPlugin {

    /**
     * Gets the instance of the plugin.
     * Do not use this method, it's only for internal use.
     *
     * @return The instance of the plugin
     */
    public static PlusLib getInstance() {
        return JavaPlugin.getPlugin(PlusLib.class);
    }

    /**
     * Gets the {@link Logger} of the plugin.
     * Do not use this method, it's only for internal use.
     *
     * @return The {@link Logger} of the plugin
     */
    public static Logger logger() {
        return PlusLib.getInstance().getLogger();
    }

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeUtil());
        Bukkit.getPluginManager().registerEvents(new BukkitUtil.Listener(), this);
        TickingManager.start();
        PlusItemManager.load();
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
        TickingManager.stop();
        PlusCommandManager.unregisterAll();
        PlusItemManager.unregisterAll();
    }

}
