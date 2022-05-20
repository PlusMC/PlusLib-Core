package org.plusmc.pluslib.reflection.config;

import org.plusmc.pluslib.reflection.BungeeSpigotReflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public interface IConfig {

    static IConfig create(File file) throws IOException {
        if (BungeeSpigotReflection.isBukkit()) {
            return new ConfigSpigot(file);
        } else if (BungeeSpigotReflection.isBungee()) {
            return new ConfigBungee(file);
        }
        throw new IllegalStateException("Unsupported server type");
    }

    default void write(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(obj, this.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object get(String key);

    default <T> T read(Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                this.set(field.getName(), field.get(obj));
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to read config");
        }
    }

    void set(String key, Object value);

    void save() throws IOException;
}
