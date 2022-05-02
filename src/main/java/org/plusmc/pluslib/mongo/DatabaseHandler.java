package org.plusmc.pluslib.mongo;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

public class DatabaseHandler {
    private final MongoClient client;
    private final Morphia morphia;
    private UserDAO userDAO;

    public DatabaseHandler() {
        client = new MongoClient(new ServerAddress("localhost", 27017));
        morphia = new Morphia();
        loadDataStore();
    }

    private void loadDataStore() {
        morphia.map(User.class);

        Datastore datastore = morphia.createDatastore(client, "DEV_PlusMCDB");
        datastore.ensureIndexes();

        userDAO = new UserDAO(User.class, datastore);
    }

    public void shutdown() {
        client.close();
    }

    public User getUser(String uuid) {
        return userDAO.findOne("uuid", uuid);
    }

    public void saveUser(User user) {
        userDAO.save(user);
    }

    public List<User> getAllUsers() {
        return userDAO.find().asList();
    }


}
