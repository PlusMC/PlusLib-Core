package org.plusmc.pluslib.bukkit.managing;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.plusmc.pluslib.bukkit.managed.Loadable;
import org.plusmc.pluslib.bukkit.managed.PlusCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to register {@link PlusCommand}.
 * Using reflection.
 */
@SuppressWarnings("unused")
public class PlusCommandManager extends BaseManager {
    private final CommandMap COMMAND_MAP = getCommandMap();
    private List<PlusCommand> COMMANDS = new ArrayList<>();

    protected PlusCommandManager(JavaPlugin plugin) {
        super(plugin);
    }

    private PluginCommand createCommand(String name, JavaPlugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        } catch (Exception e) {
            getPlugin().getLogger().severe("Failed to create PluginCommand");
        }

        return command;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            getPlugin().getLogger().severe("Failed to get CommandMap");
        }

        return commandMap;
    }

    /**
     * Registers the command.
     *
     * @param cmd Command to register
     */
    @Override
    protected void register(Loadable cmd) {
        if (!(cmd instanceof PlusCommand pCmd)) return;
        PluginCommand command = createCommand(pCmd.getName(), getPlugin());
        command.setExecutor(pCmd);
        command.setTabCompleter(pCmd);
        command.setPermission(pCmd.getPermission());
        command.setUsage(pCmd.getUsage());
        command.setDescription(pCmd.getDescription());
        COMMAND_MAP.register(getPlugin().getName(), command);
        COMMANDS.add(pCmd);
        getPlugin().getLogger().info("Registered command: " + pCmd.getName());
    }

    /**
     * Registers the command. Will be called automatically when the library plugin is unloaded.
     *
     * @param cmd Command to unload
     */
    @Override
    protected void unregister(Loadable cmd) {
        if (!(cmd instanceof PlusCommand pCmd)) return;
        CommandMap COMMAND_MAP = getCommandMap();
        PluginCommand command = getPlugin().getCommand(pCmd.getName());
        if (command == null) {
            getPlugin().getLogger().warning("Failed to unregister command: " + pCmd.getName());
            return;
        }
        command.unregister(COMMAND_MAP);
        pCmd.unload();
        getPlugin().getLogger().info("Unregistered command: " + pCmd.getName());
    }

    @Override
    protected void init() {
        COMMANDS = new ArrayList<>();
    }

    @Override
    public Class<? extends Loadable> getManaged() {
        return PlusCommand.class;
    }


    @Override
    protected void shutdown() {
        COMMANDS.forEach(this::unregister);
    }
}
