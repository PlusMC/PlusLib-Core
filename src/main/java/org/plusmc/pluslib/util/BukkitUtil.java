package org.plusmc.pluslib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * A utility class for Bukkit.
 */
@SuppressWarnings("unused")
public class BukkitUtil {
    /**
     * Get a list of online player's names
     *
     * @return List of online player's names
     */
    public static List<String> allPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
