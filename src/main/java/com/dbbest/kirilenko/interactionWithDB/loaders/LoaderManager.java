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
            throw new RuntimeException("can't obtain connection for " + dbURL + " database", e);
        }
    }

    public Node lazyLoad() {
        Loader rootLoader = null;
        for (Map.Entry<String, Loader> set : loaders.entrySet()) {
            if (set.getValue().isRoot()) {
                rootLoader = set.getValue();
                break;
            }
        }
        Node root = new Node();
        try {
            assert rootLoader != null;
            rootLoader.lazyLoad(root, connection);
            loadChildren(root, rootLoader);
        } catch (SQLException e) {
            throw new RuntimeException("can't load tree", e);
        }
        return root;
    }

    private void loadChildren(Node root, Loader parentLoader) throws SQLException {
        for (Map.Entry<String, Loader> set : loaders.entrySet()) {
            Class parent = set.getValue().getParent();
            if (parentLoader.getClass().equals(parent)) {
                Loader loader = set.getValue();
                loader.lazyLoad(root, connection);
            }
        }
    }

    public void fullLoadOnLazy(Node node) {
        for (Map.Entry<String, Loader> set : loaders.entrySet()) {
            try {
                set.getValue().fullLoadOnLazy(node, connection);
            } catch (SQLException e) {
                throw new RuntimeException("problems with full loading ...", e);
            }
        }
    }

}
