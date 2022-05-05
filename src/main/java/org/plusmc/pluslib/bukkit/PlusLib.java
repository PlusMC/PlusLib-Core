package org.plusmc.pluslib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.managing.BaseManager;
import org.plusmc.pluslib.bukkit.managing.PlusCommandManager;
import org.plusmc.pluslib.bukkit.test.GUITest;
import org.plusmc.pluslib.bukkit.util.BukkitUtil;
import org.plusmc.pluslib.bukkit.util.BungeeUtil;


import java.util.logging.Logger;


/**
 * The plugin class for the PlusLib
 * Don't use this class if you are
 */
@SuppressWarnings("unused")
public final class PlusLib extends JavaPlugin {

    public static final String BUNGEE_CORD = "BungeeCord";
    public static final String
            PLUSMC_BUNGEE = "plusmc:bungee";

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
        BungeeUtil util = new BungeeUtil();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, BUNGEE_CORD);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, PLUSMC_BUNGEE);

        Bukkit.getMessenger().registerIncomingPluginChannel(this, BUNGEE_CORD, util);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, PLUSMC_BUNGEE, util);
        Bukkit.getPluginManager().registerEvents(new BukkitUtil.Listener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, BUNGEE_CORD);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, BUNGEE_CORD);

        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, PLUSMC_BUNGEE);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, PLUSMC_BUNGEE);
    }

}
