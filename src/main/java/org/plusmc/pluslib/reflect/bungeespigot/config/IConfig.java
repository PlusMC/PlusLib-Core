package org.plusmc.pluslib.reflect.bungeespigot.config;

import org.plusmc.pluslib.reflect.bungeespigot.BungeeSpigotReflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface IConfig {

    static IConfig create(File file) throws IOException {
        if (BungeeSpigotReflection.isBukkit()) {
            return new ConfigSpigot(file);
        } else if (BungeeSpigotReflection.isBungee()) {
            return new ConfigBungee(file);
        }
        throw new IllegalStateException("Unsupported server type");
    }

    IConfig section(String section);

    default void writeIntoConfig(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                this.set(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object get(String key);

    default void writeIntoObject(Object obj) {
        try {
            for(Field field : obj.getClass().getDeclaredFields()) {
                if(!field.isAnnotationPresent(ConfigEntry.class))
                    continue;
                if(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    if(BungeeSpigotReflection.getLogger() != null)
                        BungeeSpigotReflection.getLogger().warning("Cannot read field " + field.getName() + " in " + obj.getClass().getName() + " because it is final or static");
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, this.get(field.getName()));
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to read config");
        }
    }

    File getFile();


    void set(String key, Object value);

    void save() throws IOException;
}
