package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.MySQLConnect;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TableLoaderTest {

    @Test
    public void lazyLoadWithChildren() throws SQLException {
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";
        String schema = "sakila";
        Connect connect = new MySQLConnect();
        connect.initConnection(url, login, pass);
        Connection connection = connect.getConnection();
        Loader loader = new TableLoader(connection);
        Node tables = loader.lazyLoad(schema);
        for (Node table : tables.getChildren()) {
            loader.loadElement(table);
            System.out.println(table);
        }

    }

    @Test
    public void loadElement() {
    }
}