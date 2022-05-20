package org.plusmc.pluslib.mongo;

public record DBConfig (
        boolean useMongodb,
        String host,
        int port,
        String collection,
        String username,
        String password
) {

    public DBConfig() {
        this(false, "localhost", 27017, "", "", "");
    }
}
