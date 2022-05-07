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
    private final CommandMap commandMap = getCommandMap();
    private List<PlusCommand> commands = new ArrayList<>();

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
        CommandMap map = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                map = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
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
        if (command == null) throw new IllegalStateException("Failed to create command: " + pCmd.getName());
        command.setExecutor(pCmd);
        command.setTabCompleter(pCmd);
        command.setPermission(pCmd.getPermission());
        command.setUsage(pCmd.getUsage());
        command.setDescription(pCmd.getDescription());
        commandMap.register(getPlugin().getName(), command);
        commands.add(pCmd);
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
        PluginCommand command = getPlugin().getCommand(pCmd.getName());
        if (command == null) {
            getPlugin().getLogger().warning("Failed to unregister command: " + pCmd.getName());
            return;
        }
        command.unregister(commandMap);
        pCmd.unload();
        getPlugin().getLogger().info("Unregistered command: " + pCmd.getName());
    }

    @Override
    protected void init() {
        commands = new ArrayList<>();
    }

    @Override
    public Class<? extends Loadable> getManaged() {
        return PlusCommand.class;
    }


    @Override
    protected void shutdown() {
        commands.forEach(this::unregister);
    }
}
