package org.plusmc.pluslib.reflection.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigBungee implements IConfig {
private final Configuration configuration;
    private final File file;

    public ConfigBungee(File file) throws IOException {
        this.file = file;
        this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
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
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
    }
}
