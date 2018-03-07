package com.dbbest.kirilenko.interactionWithDB.connections;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Connect {

    /**
     * connection for database
     */
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * method initialize connection for DB
     *
     * @param dbURL url of DB.
     * @param login DB login
     * @param password DB password
     * @throws SQLException if credentials are invalid
     */
    public abstract void initConnection(String dbURL, String login, String password) throws SQLException;
}
