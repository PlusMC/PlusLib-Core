package org.plusmc.pluslib.reflection.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigSpigot implements IConfig {
    private final ConfigurationSection configuration;

    private final File file;
    private final FileConfiguration fileConfiguration;

    public ConfigSpigot(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.configuration = fileConfiguration;
    }

    public ConfigSpigot(ConfigurationSection configuration, ConfigSpigot parent) {
        this.configuration = configuration;
        this.file = parent.getFile();
        this.fileConfiguration = parent.getFileConfiguration();
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    @Override
    public IConfig section(String section) {
        return new ConfigSpigot(configuration.getConfigurationSection(section), this);
    }

    @Override
    public Object get(String key) {
        return configuration.get(key);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void set(String key, Object value) {
        if(configuration.getCurrentPath() != null)
            fileConfiguration.set(configuration.getCurrentPath(), value);
        else configuration.set(key, value);
    }

    @Override
    public void save() throws IOException {
        fileConfiguration.save(file);
    }
}

