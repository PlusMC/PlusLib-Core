package org.plusmc.pluslib.bukkit.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableHandler {

    private VariableHandler() {
    }

    private static final Map<String, String> variables = new HashMap<>();

    public static void setVariable(final String key, final String value) {
        variables.put(key, value);
    }

    public static void removeVariable(final String key) {
        variables.remove(key);
    }

    public static String getVariable(final String key) {
        return variables.get(key);
    }

    public static String formatString(String string) {
        final ArrayList<String> keys = new ArrayList<>(variables.keySet());
        for(final String key : keys) {
            final String value = variables.get(key);
            string = string.replace("%" + key + "%", value);
        }
        return string;
    }
}
