package org.plusmc.pluslib.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.plus.PlusCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register {@link PlusCommand}.
 * Using reflection.
 */
@SuppressWarnings("unused")
public class PlusCommandManager {
    private static final CommandMap COMMAND_MAP = getCommandMap();
    private static final List<PlusCommand> COMMANDS = new ArrayList<>();

    /**
     * Registers the command.
     *
     * @param cmd Command to register
     */
    public static void register(@NotNull PlusCommand cmd) {
        PluginCommand command = createCommand(cmd.getName(), cmd.getPlugin());
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);
        command.setPermission(cmd.getPermission());
        command.setUsage(cmd.getUsage());
        command.setDescription(cmd.getDescription());
        COMMAND_MAP.register(cmd.getPlugin().getName(), command);
        COMMANDS.add(cmd);
        cmd.load();
        PlusLib.logger().info("Loaded command: " + cmd.getName());
    }

    /**
     * Unregisters all commands.
     */
    protected static void unregisterAll() {
        COMMANDS.forEach(PlusCommandManager::unregister);
    }

    /**
     * Registers the command. Will be called automatically when the library plugin is unloaded.
     *
     * @param cmd Command to unload
     */
    public static void unregister(@NotNull PlusCommand cmd) {
        CommandMap COMMAND_MAP = getCommandMap();
        PluginCommand command = cmd.getPlugin().getCommand(cmd.getName());
        if (command == null) {
            PlusLib.logger().warning("Failed to unload command: " + cmd.getName());
            return;
        }
        command.unregister(COMMAND_MAP);
        cmd.unload();
        PlusLib.logger().info("Unloaded command: " + cmd.getName());
    }

    private static PluginCommand createCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        } catch (Exception e) {
            PlusLib.logger().severe("Failed to create PluginCommand");
        }

        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            PlusLib.logger().severe("Failed to get CommandMap");
        }

        return commandMap;
    }
}
