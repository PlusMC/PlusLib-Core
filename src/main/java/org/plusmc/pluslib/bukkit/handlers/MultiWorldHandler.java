package org.plusmc.pluslib.bukkit.handlers;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.plusmc.pluslib.bukkit.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.END_PORTAL;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;

/**
 * @author OakleyCord
 * Useful for making a world with end and nether portals.
 */
public class MultiWorldHandler {
    private final World worldOverworld;
    private final JavaPlugin plugin;
    private final MultiWorldListener worldListener;
    private World worldNether;
    private World worldEnd;

    public MultiWorldHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        worldOverworld = new WorldCreator("world").createWorld();
        this.worldListener = new MultiWorldListener();
        plugin.getServer().getPluginManager().registerEvents(worldListener, plugin);
    }

    public MultiWorldHandler(JavaPlugin plugin, World worldOverworld, World worldNether, World worldEnd) {
        this.plugin = plugin;
        this.worldListener = new MultiWorldListener();
        plugin.getServer().getPluginManager().registerEvents(worldListener, plugin);
        this.worldOverworld = worldOverworld;
        this.worldNether = worldNether;
        this.worldEnd = worldEnd;
    }

    public List<World> getWorlds() {
        List<World> worlds = new ArrayList<>();
        worlds.add(worldOverworld);
        if(worldNether != null)
            worlds.add(worldNether);

        if(worldEnd != null)
            worlds.add(worldEnd);
        return worlds;
    }

    /**
     * unloads the world and deletes the folder, if players are in the world, they will be teleported to the main world
     * also its async because why not
     */
    public void delete() {
        HandlerList.unregisterAll(worldListener);
        for(World world : getWorlds()) {
            File file = world.getWorldFolder();
            world.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(world, false);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> FileUtil.deleteDir(file));
        }

    }

    /**
     * unloads the world and deletes the folder, if players is are the world, they will be teleported to the main world
     * non async version of @{@link #delete()}
     */
    public void deleteSync() {
        HandlerList.unregisterAll(worldListener);
        for(World world : getWorlds()) {
            File file = world.getWorldFolder();
            world.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(world, false);
            FileUtil.deleteDir(file);
        }
    }

    public World getWorldOverworld() {
        return worldOverworld;
    }

    @Nullable
    public World getNether() {
        return worldNether;
    }

    @Nullable
    public World getEnd() {
        return worldEnd;
    }

    public boolean hasWorld(World world) {
        return this.worldOverworld.getName().equals(world.getName()) || this.worldNether.getName().equals(world.getName()) || this.worldEnd.getName().equals(world.getName());
    }

    private class MultiWorldListener implements Listener {
        @EventHandler
        public void onPortalTeleport(PlayerPortalEvent e) {
            World from = e.getFrom().getWorld();
            if (from == null || !hasWorld(from)) return;
            World overworld = getWorldOverworld();
            World nether = getNether();
            World end = getEnd();

            Location fromLoc = e.getFrom();

            if (e.getCause() == NETHER_PORTAL) {
                if (nether == null) {
                    e.setCancelled(true);
                    return;
                }
                if (from.getEnvironment() == World.Environment.NORMAL) {
                    fromLoc.multiply(1 / 8D);
                    fromLoc.setWorld(nether);
                    e.setTo(fromLoc);
                } else if (from.getEnvironment() == World.Environment.NETHER) {
                    fromLoc.multiply(8.0D);
                    fromLoc.setWorld(overworld);
                    e.setTo(fromLoc);
                }
            } else if (e.getCause() == END_PORTAL) {
                if (end == null) {
                    e.setCancelled(true);
                    return;
                }
                if (from.getEnvironment() == World.Environment.NORMAL || from.getEnvironment() == World.Environment.NETHER)
                    e.setTo(new Location(end, 100.5, 49, 0.5));
                else if (from.getEnvironment() == World.Environment.THE_END)
                    e.setTo(overworld.getSpawnLocation().add(0.5, 1, 0.5));

            }
        }

        @EventHandler
        public void onEntityPortal(EntityPortalEvent e) {
            World from = e.getFrom().getWorld();

            if (from == null) return;
            if (!hasWorld(from)) return;
            World overworld = getWorldOverworld();
            World nether = getNether();
            World end = getEnd();

            Location fromLoc = e.getFrom();

            if (e.getFrom().getBlock().getType() == Material.NETHER_PORTAL) {
                if (nether == null) {
                    e.setCancelled(true);
                    return;
                }
                if (from.getEnvironment() == World.Environment.NORMAL) {
                    fromLoc.multiply(1 / 8D);
                    fromLoc.setWorld(nether);
                    e.setTo(fromLoc);
                } else if (from.getEnvironment() == World.Environment.NETHER) {
                    fromLoc.multiply(8.0D);
                    fromLoc.setWorld(overworld);
                    e.setTo(fromLoc);
                }
            } else if (e.getFrom().getBlock().getType() == Material.END_PORTAL) {
                if (end == null) {
                    e.setCancelled(true);
                    return;
                }

                if (from.getEnvironment() == World.Environment.NORMAL || from.getEnvironment() == World.Environment.NETHER)
                    e.setTo(new Location(end, 100.5, 49, 0.5));
                else if (from.getEnvironment() == World.Environment.THE_END)
                    e.setTo(overworld.getSpawnLocation().add(0.5, 1, 0.5));
            }
        }

    }
}
