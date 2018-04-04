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

    public DBType getType() {
        return type;
    }

    private Connection connection;

    private final Map<String, Loader> loaders;

    private static LoaderManager instance;

    public static synchronized LoaderManager getInstance(DBType type, String url, String login, String pass) throws SQLException {
        if (instance == null) {
            instance = new LoaderManager(type, url, login, pass);
        }
        return instance;
    }

    public static synchronized LoaderManager getInstance() {
        return instance;
    }

    public static void clearManager() throws SQLException {
        instance.connection.close();
        instance = null;
    }

    private LoaderManager(DBType type, String url, String login, String pass) throws SQLException {
        this.type = type;
        initConnection(url, login, pass);
        loaders = ReflectionUtil.obtainMap(type, EntityLoader.class);
    }

    private void initConnection(String dbURL, String login, String pass) throws SQLException {
        Connect connect = ConnectFactory.getConnect(type);

        connect.initConnection(dbURL, login, pass);
        connection = connect.getConnection();
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

    /**
     * Fully loads given node
     *
     * @param node for loading
     * @return fully loaded node
     */
    public Node fullLoadElement(Node node) {
        return load((n, l) ->
                l.fullLoadElement(n), node);
    }

    /**
     * Lazy loads children for given node.
     *
     * @param node for lazy children loading
     * @return node with loaded children
     */
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

    @Override
    public String toString() {
        try {
            return "manager connected to " + connection.getCatalog();
        } catch (SQLException e) {
            return "no connection";
        }
    }
}
