package org.plusmc.pluslib.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.plusmc.pluslib.reflection.BungeeSpigotReflection;

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
    private final DBConfig config;

    private DatabaseHandler(DBConfig config) {
        this.config = config;
        if(!config.useMongodb()) return;

        BungeeSpigotReflection.runAsync(() -> {
            try {
                if(config.collection().isBlank() || config.host().isBlank() || config.port() == 0) {
                    throw new IllegalArgumentException("Invalid mongodb configuration");
                }
                MongoClientOptions options = MongoClientOptions.builder().serverSelectionTimeout(5000).build();
                ServerAddress address = new ServerAddress(config.host(), config.port());

                if(config.username().isBlank() && config.password().isBlank()) {
                    client = new MongoClient(address, options);
                } else {
                    MongoCredential credential = MongoCredential.createCredential(config.username(), config.collection(), config.password().toCharArray());
                    client = new MongoClient(address, credential, options);
                }


                morphia = new Morphia();
                loadDataStore();
                if(BungeeSpigotReflection.getLogger() != null)
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

    private void loadDataStore() {
        if(morphia == null) return;
        morphia.map(User.class);

        Datastore datastore = morphia.createDatastore(client, config.collection());
        datastore.ensureIndexes();

        userDAO = new UserDAO(User.class, datastore);
    }

    public boolean isLoaded() {
        return client != null && morphia != null && userDAO != null;
    }

    public static void createInstance(DBConfig config) {
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
        if (!isLoaded())
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

    public User getUserSync(UUID uuid) {
        return getUser(uuid);
    }

    public void saveUserSync(User user) {
        saveUser(user);
    }
}
