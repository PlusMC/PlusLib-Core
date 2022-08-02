package org.plusmc.pluslibcore.reflect.bungeespigot.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigBungee implements IConfig {
    private final Configuration configuration;
    private final IConfig parent;
    private final File file;
    private final String section;

    public ConfigBungee(File file) throws IOException {
        this.file = file;
        this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        this.parent = null;
        this.section = null;
    }

    public ConfigBungee(Configuration configuration, File file) {
        this.configuration = configuration;
        this.file = file;
        this.parent = null;
        this.section = null;
    }

    public ConfigBungee(IConfig parent, Configuration configuration, String section) {
        this.configuration = configuration;
        this.parent = parent;
        this.file = parent.getFile();
        this.section = section;
    }

    @Override
    public IConfig section(String section) {
        return new ConfigBungee(this, configuration.getSection(section), section);
    }

    @Override
    public void set(String key, Object value) {
        if (parent != null)
            parent.set(section + "." + key, value);
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
    public void save() throws IOException {
        if (parent == null)
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        else parent.save();
    }

}
