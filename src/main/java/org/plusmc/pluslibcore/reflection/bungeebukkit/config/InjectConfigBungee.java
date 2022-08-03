package org.plusmc.pluslibcore.reflection.bungeebukkit.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class InjectConfigBungee implements InjectableConfig {
    private final Configuration configuration;
    private final InjectableConfig parent;
    private final File file;
    private final String section;

    public InjectConfigBungee(File file) throws IOException {
        this.file = file;
        this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        this.parent = null;
        this.section = null;
    }

    public InjectConfigBungee(Configuration configuration, File file) {
        this.configuration = configuration;
        this.file = file;
        this.parent = null;
        this.section = null;
    }

    public InjectConfigBungee(InjectableConfig parent, Configuration configuration, String section) {
        this.configuration = configuration;
        this.parent = parent;
        this.file = parent.getFile();
        this.section = section;
    }

    @Override
    public InjectableConfig section(String section) {
        return new InjectConfigBungee(this, configuration.getSection(section), section);
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
    public void readObject() throws IOException {
        if (parent == null)
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        else parent.readObject();
    }

}
