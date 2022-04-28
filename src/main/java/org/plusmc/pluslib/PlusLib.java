package org.plusmc.pluslib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.managing.BaseManager;
import org.plusmc.pluslib.managing.GUIManager;
import org.plusmc.pluslib.managing.PlusCommandManager;
import org.plusmc.pluslib.test.GUITest;
import org.plusmc.pluslib.util.BukkitUtil;
import org.plusmc.pluslib.util.BungeeUtil;

import java.util.logging.Logger;


/**
 * The plugin class for the PlusLib
 * Don't use this class if you are
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
        BaseManager.createManager(PlusCommandManager.class, this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeUtil());
        Bukkit.getPluginManager().registerEvents(new BukkitUtil.Listener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
    }

}
