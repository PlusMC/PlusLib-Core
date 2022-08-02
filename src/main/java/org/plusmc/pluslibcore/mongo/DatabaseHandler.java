package org.plusmc.pluslibcore.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.plusmc.pluslibcore.reflect.bungeespigot.BungeeSpigotReflection;
import org.plusmc.pluslibcore.reflect.bungeespigot.config.ConfigEntry;
import org.plusmc.pluslibcore.reflect.bungeespigot.config.IConfig;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DatabaseHandler {
    @Nullable
    private static DatabaseHandler instance;
    private MongoClient client;
    private Morphia morphia;
    private List<User> cachedUsers;
    private UserDAO userDAO;


    @ConfigEntry
    private boolean useMongodb;
    @ConfigEntry
    private String host;
    @ConfigEntry
    private int port;
    @ConfigEntry
    private String collection;
    @ConfigEntry
    private String username;
    @ConfigEntry
    private String password;

    private DatabaseHandler(IConfig config) {
        config.writeIntoObject(this);
        if (!useMongodb) {
            if (BungeeSpigotReflection.getLogger() != null)
                BungeeSpigotReflection.getLogger().info("Mongodb is disabled, skipping database setup");
            return;
        }

        BungeeSpigotReflection.runAsync(() -> {
            try {
                if (collection.isBlank() || host.isBlank() || port == 0)
                    throw new IllegalArgumentException("Invalid Mongodb Configuration");
                loadMongodb();
                loadDataStore();
                if (BungeeSpigotReflection.getLogger() != null)
                    BungeeSpigotReflection.getLogger().info("Connected to MongoDB");
            } catch (Exception e) {
                if (BungeeSpigotReflection.getLogger() != null)
                    BungeeSpigotReflection.getLogger().warning("Failed to connect to database!");
                e.printStackTrace();
                client.close();
                client = null;
                morphia = null;
                userDAO = null;
            }
        });
    }

    private void loadMongodb() {
        MongoClientOptions options = MongoClientOptions.builder().serverSelectionTimeout(5000).build();
        ServerAddress address = new ServerAddress(host, port);

        if (username.isBlank() && password.isBlank()) {
            client = new MongoClient(address, options);
        } else {
            MongoCredential credential = MongoCredential.createCredential(username, collection, password.toCharArray());
            client = new MongoClient(address, credential, options);
        }
    }

    private void loadDataStore() {
        morphia = new Morphia();
        morphia.map(User.class);

        Datastore datastore = morphia.createDatastore(client, collection);
        datastore.ensureIndexes();

        userDAO = new UserDAO(User.class, datastore);
    }

    public static void createInstance(IConfig config) {
        if (instance == null)
            instance = new DatabaseHandler(config);

    }

    public static @Nullable DatabaseHandler getInstance() {
        return instance;
    }

    public void updateCache() {
        BungeeSpigotReflection.runAsync(() ->
                cachedUsers = userDAO.find().asList()
        );
    }

    /**
     * Asynchronously executes the given action on the user with the given UUID.
     * Saves the user if the action modifies it.
     *
     * @param uuid   UUID of the user to execute the action on
     * @param action action to execute
     */
    public void asyncUserAction(UUID uuid, Consumer<User> action) {
        if (!isLoaded())
            return;
        BungeeSpigotReflection.runAsync(() -> {
            User user = getUser(uuid);
            if (user != null)
                action.accept(user);
            saveUser(user);
        });
    }

    public boolean isLoaded() {
        return client != null && morphia != null && userDAO != null;
    }

    private User getUser(UUID uuid) {
        if (!isLoaded())
            return null;
        return userDAO.findOne("uuid", uuid.toString());
    }

    private void saveUser(User user) {
        if (!isLoaded())
            return;
        userDAO.save(user);
    }

    /**
     * Gets the user cache.
     * The cache is updated every 30 seconds, so it may be out of date.
     * Do not update any user objects in the cache, it will not be saved.
     *
     * @return a list of all users in the cache.
     */
    public List<User> getUserCache() {
        if (!isLoaded() || cachedUsers == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(cachedUsers);
    }

    /**
     * Gets the user with the given UUID from the cache.
     * The cache is updated every 30 seconds, so it may be out of date.
     * Do not update any user objects in the cache, it will not be saved.
     *
     * @param uuid UUID of the user to get from the cache.
     * @return user if found, null if not.
     */
    @Nullable
    public User getUserFromCache(UUID uuid) {
        if (!isLoaded())
            return null;
        for (User user : cachedUsers) {
            if (user.getUUID().equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    @Nullable
    public User getUserFromCache(String name) {
        if (!isLoaded())
            return null;
        for (User user : cachedUsers) {
            if (user.getName().equals(name)) return user;
        }
        return null;
    }

    public User getUserSync(UUID uuid) {
        return getUser(uuid);
    }

    public void saveUserSync(User user) {
        saveUser(user);
    }
}
