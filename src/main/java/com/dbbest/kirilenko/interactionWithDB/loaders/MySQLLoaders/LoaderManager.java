package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.Connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoadersInitializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private Connection connection;
    private LoadersInitializer initializer;

    public LoaderManager(DBType type, String dbURL, String login, String pass) {
        try {
            initializer = new LoadersInitializer(type);
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("can't initialize loaders: ", e);
        }

        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(dbURL, login, pass);
            connection = connect.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("can't obtain connection for " + dbURL + " database", e);
        }
    }

    public Node lazyLoad() {
        Map<DBElement, Loader> loaders = initializer.getLoaders();
        Loader schemaLoader = loaders.get(DBElement.SCHEMA);
        try {
            return schemaLoader.lazyLoad(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
