package com.dbbest.kirilenko.interactionWithDB.Connections;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect extends Connect {

    @Override
    public void initConnection(String url, String login, String password) throws SQLException {
        connection = DriverManager.getConnection(url, login, password);
    }

}
