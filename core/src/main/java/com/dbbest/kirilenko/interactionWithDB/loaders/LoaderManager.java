package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private Node root;

    private final Map<String, Loader> loaders;

    public LoaderManager(DBType type, String dbURL, String login, String pass) {
        loaders = ReflectionUtil.obtain(type, Loader.class);

        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(dbURL, login, pass);
            Connection connection = connect.getConnection();
            for (Map.Entry<String, Loader> set : loaders.entrySet()) {
                set.getValue().setConnection(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("can't connect to " + dbURL + " database", e);
        }
        switch (type) {
            case MYSQL:
                root = new Node(DBElement.SCHEMA);
        }
    }

    public Node lazyDBLoad(String dataBase) {
        Loader loader = loaders.get(root.getName());
        try {
            root = loader.lazyLoad(dataBase);
        } catch (SQLException e) {
            throw new RuntimeException("something went wrong", e);
        }
        return root;
    }

    public void loadElement(Node node) {
        Loader loader = loaders.get(node.getName());
        try {
            loader.loadElement(node);
        } catch (SQLException e) {
            throw new RuntimeException("can't load element: " + node);
        }
    }
}
