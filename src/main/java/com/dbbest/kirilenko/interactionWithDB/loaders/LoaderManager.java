package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.Connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.DBType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private Connection connection;

    private final Map<String, Loader> loaders;

    public LoaderManager(DBType type, String dbURL, String login, String pass) {
        try {
            LoadersInitializer initializer = new LoadersInitializer(type);
            loaders = initializer.getLoaders();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("can't initialize loaders: ", e);
        }

        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(dbURL, login, pass);
            connection = connect.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("can't connect to " + dbURL + " database", e);
        }
    }

    public Node lazyDBLoad(String schema) {
        Loader root = null;
        for (Map.Entry<String, Loader> set : loaders.entrySet()) {
            if (set.getValue().getParent() == null) {
                root = set.getValue();
                root.setConnection(connection);
                break;
            }
        }
        try {
            assert root != null;
            Node schemaNode = root.lazyLoad(schema);
            for (Map.Entry<String, Loader> set : loaders.entrySet()) {
                Loader childLoader = set.getValue();
                if (childLoader.getClass() != root.getClass()) {
                    childLoader.setConnection(connection);
                    Node childNode = childLoader.lazyLoad(schema);
                    schemaNode.addChild(childNode);
                }
            }
            return schemaNode;
        } catch (SQLException e) {
            throw new RuntimeException("problems with loader ", e);
        }
    }


}
