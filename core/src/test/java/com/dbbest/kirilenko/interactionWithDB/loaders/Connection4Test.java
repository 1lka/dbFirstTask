package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.MySQLConnect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Connection4Test {

    private final static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    private final static String url = "jdbc:mysql://localhost";
    private final static String login = "root";
    private final static String pass = "root";

    static {
        Connect connect = new MySQLConnect();
        try {
            connect.initConnection(url,"3306", login, pass);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        connection = connect.getConnection();
    }
}
