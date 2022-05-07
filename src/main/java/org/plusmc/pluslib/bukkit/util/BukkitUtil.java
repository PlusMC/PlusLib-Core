package org.plusmc.pluslib.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.plusmc.pluslib.bukkit.PlusLibBukkit;

import java.util.HashMap;
import java.util.List;

/**
 * A utility class for Bukkit.
 */
@SuppressWarnings("unused")
public class BukkitUtil {
    private static final HashMap<Player, Long> JOIN_TIMES = new HashMap<>();


    private BukkitUtil() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Get a list of online player's names
     *
     * @return List of online player's names
     */
    public static List<String> allPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    /**
     * Get the time since the player joined the server
     *
     * @param player Player to get
     * @return Time since the player joined the server
     */
    public static long getTimeSinceJoin(Player player) {
        return System.currentTimeMillis() - JOIN_TIMES.getOrDefault(player, System.currentTimeMillis());
    }

    /**
     * Listener for {@link BukkitUtil} (DO NOT REGISTER THIS LISTENER FOR INTERNAL USE ONLY)
     */
    public static class Listener implements org.bukkit.event.Listener {
        /**
         * Event handler for {@link PlayerJoinEvent}
         *
         * @param event Event to handle
         */

        boolean isFirst = true;
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            JOIN_TIMES.put(player, System.currentTimeMillis());
            if(isFirst) {
                Bukkit.getScheduler().runTask(PlusLibBukkit.getInstance(), BungeeUtil::checkPlusMC);
                isFirst = false;
            }
        }
    }
}
