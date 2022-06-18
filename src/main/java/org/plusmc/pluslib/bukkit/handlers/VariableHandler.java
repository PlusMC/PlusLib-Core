package org.plusmc.pluslib.bukkit.handlers;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VariableHandler {

    private static final Map<String, String> variables = new HashMap<>();
    private static final Map<UUID, Map<String, String>> playerVariables = new HashMap<>();

    private VariableHandler() {
    }

    public static void setVariable(String key, String value) {
        variables.put(key, value);
    }

    public static void setVariable(OfflinePlayer player, String key, String value) {
        setVariable(player.getUniqueId(), key, value);
    }

    public static void setVariable(UUID uuid, String key, String value) {
        playerVariables.computeIfAbsent(uuid, k -> new HashMap<>());
        playerVariables.get(uuid).put(key, value);
    }

    public static void removeVariable(String key) {
        variables.remove(key);
    }

    public static String getVariable(String key) {
        return variables.get(key);
    }

    public static String format(String string, OfflinePlayer player, boolean useNonPlayerVariables) {
        return formatString(string, player.getUniqueId(), useNonPlayerVariables);
    }

    public static String formatString(String string, UUID uuid, boolean useNonPlayerVariables) {
        Map<String, String> variables = playerVariables.getOrDefault(uuid, new HashMap<>());
        for (Map.Entry<String, String> entry : variables.entrySet())
            string = string.replace("%" + entry.getKey() + "%", entry.getValue());

        if (useNonPlayerVariables)
            string = formatString(string);
        return string;
    }

    public static String formatString(String string) {
        for (Map.Entry<String, String> entry : variables.entrySet())
            string = string.replace("%" + entry.getKey() + "%", entry.getValue());

        return string;
    }
}
