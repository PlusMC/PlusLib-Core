package org.plusmc.pluslibcore.reflection.bungeebukkit.config;

import org.plusmc.pluslibcore.reflection.bungeebukkit.BungeeBukkitReflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface InjectableConfig {

    static InjectableConfig create(File file) throws IOException {
        if (BungeeBukkitReflection.isBukkit()) {
            return new InjectConfigBukkit(file);
        } else if (BungeeBukkitReflection.isBungee()) {
            return new InjectConfigBungee(file);
        }
        throw new IllegalStateException("Unsupported server type");
    }

    InjectableConfig section(String section);

    default void readObject(Object obj) {
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

    void set(String key, Object value);

    default void inject(Object obj) {
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigEntry.class))
                    continue;
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    if (BungeeBukkitReflection.getLogger() != null)
                        BungeeBukkitReflection.getLogger().warning("Cannot read field " + field.getName() + " in " + obj.getClass().getName() + " because it is final or static");
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

    Object get(String key);

    File getFile();

    void readObject() throws IOException;
}
