package com.dbbest.kirilenko.interactionWithDB.connections;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Connect {

    Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public abstract void initConnection(String dbURL, String login, String password) throws SQLException;
}
