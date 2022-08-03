package org.plusmc.pluslibcore.reflection.bungeebukkit.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class InjectConfigBukkit implements InjectableConfig {
    private final ConfigurationSection configuration;

    private final File file;
    private final FileConfiguration fileConfiguration;

    public InjectConfigBukkit(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.configuration = fileConfiguration;
    }

    public InjectConfigBukkit(ConfigurationSection configuration, InjectConfigBukkit parent) {
        this.configuration = configuration;
        this.file = parent.getFile();
        this.fileConfiguration = parent.getFileConfiguration();
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    @Override
    public InjectableConfig section(String section) {
        return new InjectConfigBukkit(configuration.getConfigurationSection(section), this);
    }

    @Override
    public void set(String key, Object value) {
        if (configuration.getCurrentPath() != null)
            fileConfiguration.set(configuration.getCurrentPath(), value);
        else configuration.set(key, value);
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
    public void readObject() throws IOException {
        fileConfiguration.save(file);
    }
}

