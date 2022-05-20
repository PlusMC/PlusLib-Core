package org.plusmc.pluslib.reflection.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigSpigot implements IConfig {
    private final FileConfiguration configuration;
    private final File file;

    public ConfigSpigot(File file) {
        this.file = file;
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public Object get(String key) {
        return configuration.get(key);
    }

    @Override
    public void set(String key, Object value) {
        configuration.set(key, value);
    }

    @Override
    public void save() throws IOException {
        configuration.save(file);
    }
}

