package org.plusmc.pluslib.managed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.plusmc.pluslib.managing.PlusCommandManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that can be registered to a {@link JavaPlugin} using {@link PlusCommandManager#register(PlusCommand)}.
 * Useful for making complicated commands easier to write.
 */
@SuppressWarnings("unused")
public interface PlusCommand extends CommandExecutor, TabCompleter, Loadable {
    /**
     * Gets the command name.
     *
     * @return Command name
     */
    String getName();

    /**
     * Gets the permission required to use this command.
     *
     * @return Permission required to use this command
     */
    String getPermission();

    /**
     * Gets the usage of this command.
     *
     * @return Usage of this command
     */
    String getUsage();

    /**
     * Gets the description of this command.
     *
     * @return Description of this command
     */
    String getDescription();

    /**
     * Gets the plugin that this command belongs to.
     *
     * @return Plugin that this command belongs to
     */
    JavaPlugin getPlugin();

    /**
     * Gets the completions of the command.
     *
     * @param index Index of the current argument
     * @return Completions of the command
     */
    List<String> getCompletions(int index);

    /**
     * Filters the completions for the given argument.
     *
     * @param arg   Current argument
     * @param index Current index
     * @return List of filtered completions
     */
    @NotNull
    default List<String> filterCompletions(String arg, int index) {
        String arg2 = arg.toLowerCase();
        List<String> completions = getCompletions(index) == null ? new ArrayList<>() : getCompletions(index);
        List<String> filtered = new ArrayList<>();
        completions.forEach(s -> {
            if (s.toLowerCase().startsWith(arg2))
                filtered.add(s);
        });
        return filtered;
    }

    @Override
    default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 0)
            return new ArrayList<>();
        return filterCompletions(args[args.length - 1], args.length);
    }
}
