package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private DBType type;

    private Connection connection;

    private final Map<String, Loader> loaders;

    public LoaderManager(DBType type, String url, String login, String pass) {
        this.type = type;
        initConnection(url, login, pass);
        loaders = ReflectionUtil.obtain(type, EntityLoader.class);
    }

    private void initConnection(String dbURL, String login, String pass) {
        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(dbURL, login, pass);
            connection = connect.getConnection();
        } catch (SQLException e) {
            throw new LoadingException("can't obtain connection to DB: " + dbURL, e);
        }
    }

    /**
     * fill attributes for given node
     *
     * @param node for attribute loading
     * @return node
     */
    public Node loadElement(Node node) {
        return load((n, l) ->
                l.loadElement(n), node);
    }

    public Node fullLoadElement(Node node) {
        return load((n, l) ->
                l.fullLoadElement(n), node);
    }

    public Node lazyChildrenLoad(Node node) {
        return load((n, l) ->
                l.lazyChildrenLoad(n), node);
    }

    private Node load(LoadingInterface lambda, Node node) {
        try {
            Loader loader = loaders.get(node.getName());
            loader.setConnection(connection);
            return lambda.load(node, loader);
        } catch (NullPointerException e) {
            throw new LoadingException("can't get loader for " + node.getName() + " node");
        } catch (SQLException e) {
            throw new LoadingException("can't load " + node, e);
        }
    }

    @FunctionalInterface
    private interface LoadingInterface {
        Node load(Node node, Loader loader) throws SQLException;
    }
}
