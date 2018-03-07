package com.dbbest.kirilenko.interactionWithDB.connections;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect extends Connect {

    /**
     * method initialize connection for MySQL DB
     *
     * @param url of DB.
     * @param login DB login
     * @param password DB password
     * @throws SQLException if credentials are invalid
     */
    @Override
    public void initConnection(String url, String login, String password) throws SQLException {
        setConnection(DriverManager.getConnection(url, login, password));
    }
}
