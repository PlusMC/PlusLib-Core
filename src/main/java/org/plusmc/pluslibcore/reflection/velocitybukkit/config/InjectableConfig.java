package org.plusmc.pluslibcore.reflection.velocitybukkit.config;

import org.plusmc.pluslibcore.reflection.velocitybukkit.VelocityBukkitReflection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface InjectableConfig {

    static InjectableConfig create(File file) throws IOException {
        if (VelocityBukkitReflection.isBukkit()) {
            return new InjectConfigBukkit(file);
        } else if (VelocityBukkitReflection.isVelocity()) {
            return new InjectConfigVelocity(file);
        }
        throw new IllegalStateException("Unsupported server type");
    }

    InjectableConfig section(String section);

    /**
     * Reads an object and injects it into the config
     * @param obj The object to inject
     */
    default void fromObject(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(ConfigEntry.class))
                    continue;
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
                    if (VelocityBukkitReflection.getLogger() != null)
                        VelocityBukkitReflection.getLogger().warning("Cannot write field " + field.getName() + " in " + obj.getClass().getName() + " because it is final or static");
                    continue;
                }

                field.setAccessible(true);
                Object value = this.get(field.getName());
                if (value == null)
                    continue;

                field.set(obj, value);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to read config");
        }
    }

    Object get(String key);

    File getFile();

    /**
     * Saves the config to the file asynchronously
     */
    void save() throws IOException;

    /**
     * Saves the config to the file
     * @param async Whether to save the config asynchronously
     * @throws IOException If an error occurs while saving the config
     */
    void save(boolean async) throws IOException;
}
