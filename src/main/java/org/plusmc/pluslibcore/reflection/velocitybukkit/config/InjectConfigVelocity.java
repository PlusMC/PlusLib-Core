package org.plusmc.pluslibcore.reflection.velocitybukkit.config;



import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class InjectConfigVelocity implements InjectableConfig {
    private final MapWrapper configuration;
    private final InjectableConfig parent;
    private final File file;
    private final String section;

    public InjectConfigVelocity(File file) throws IOException {
        this.file = file;
        this.configuration = new MapWrapper(new Yaml().load(new FileInputStream(file)));
        this.parent = null;
        this.section = null;
    }

    public InjectConfigVelocity(InjectableConfig parent, MapWrapper configuration, String section) {
        this.configuration = configuration;
        this.parent = parent;
        this.file = parent.getFile();
        this.section = section;
    }

    @Override
    public InjectableConfig section(String section) {
        return new InjectConfigVelocity(this, configuration.getSection(section), section);
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
            configuration.save(file);
        else parent.save();
    }

}
