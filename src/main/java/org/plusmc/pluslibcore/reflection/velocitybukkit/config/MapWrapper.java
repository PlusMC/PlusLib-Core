package org.plusmc.pluslibcore.reflection.velocitybukkit.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

public class MapWrapper {

    Map<Object,Object> map;

    public MapWrapper(Map<Object, Object> map) {
        this.map = map;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void set(String key, Object value) {
        if(value instanceof MapWrapper)
            map.put(key, ((MapWrapper) value).getMap());
        else map.put(key, value);
    }

    public void save(File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        yaml.dump(map, new OutputStreamWriter(new FileOutputStream(file)));
    }

    public MapWrapper getSection(String key) {
        if(map.get(key) instanceof Map)
            return new MapWrapper((Map<Object, Object>) map.get(key));
        throw new IllegalArgumentException("Key " + key + " is not a section");
    }

    private Map<Object, Object> getMap() {
        return map;
    }
}
