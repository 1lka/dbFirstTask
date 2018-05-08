package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;
import com.dbbest.kirilenko.tree.Node;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private static final Logger logger = Logger.getLogger(LoaderManager.class);

    private DBType type;

    private String DBName;

    private String url;

    private String login;

    private Connection connection;

    private Map<String, Loader> loaders;

    private Connect connect;

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getDBName() {
        return DBName;
    }

    public void setDBName(String DBName) {
        this.DBName = DBName;
    }

    public DBType getType() {
        return type;
    }

    public void setType(DBType type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Loader> getLoaders() {
        return loaders;
    }

    public Connect getConnect() {
        return connect;
    }

    public LoaderManager() {
        logger.debug("created instance of empty LoaderManager");
    }

    public LoaderManager(Connect connect) {
        this.connect = connect;
        this.type = connect.getType();
        this.url = connect.getUrl();
        this.login = connect.getLogin();
        this.DBName = connect.getDbName();
        this.connection = connect.getConnection();
        loaders = ReflectionUtil.obtainAnnotatedClasses(type, EntityLoader.class);
    }

    /**
     * fill attributes for given node
     *
     * @param node for attribute loading
     * @return node
     */
    public Node loadElement(Node node) {
        if (!Boolean.valueOf(node.getAttrs().get(Loader.ELEMENT_LOADED))) {
            load((n, l) ->
                    l.loadElement(n), node);
            node.getAttrs().put(Loader.ELEMENT_LOADED, String.valueOf(true));
        }
        return node;
    }

    /**
     * Fully loads given node
     *
     * @param node for loading
     * @return fully loaded node
     */
    public Node fullLoadElement(Node node) {
        if (!Boolean.valueOf(node.getAttrs().get(Loader.FULLY_LOADED))) {
            load((n, l) ->
                    l.fullLoadElement(n), node);
            markAllFullyLoaded(node);
        }
        return node;
    }

    /**
     * Lazy loads children for given node.
     *
     * @param node for lazy children loading
     * @return node with loaded children
     */
    public Node lazyChildrenLoad(Node node) {
        if (!Boolean.valueOf(node.getAttrs().get(Loader.LAZILY_LOADED))) {
            load((n, l) -> {
                l.lazyChildrenLoad(n);
                return loadElement(node);
            }, node);
            node.getAttrs().put(Loader.LAZILY_LOADED, String.valueOf(true));
        }
        return node;
    }

    private void load(LoadingInterface lambda, Node node) {
        try {
            Loader loader = loaders.get(node.getName());
            loader.setConnection(connection);
            lambda.load(node, loader);
        } catch (NullPointerException e) {
            throw new LoadingException("can't get loader for " + node.getName() + " node");
        } catch (SQLException e) {
            throw new LoadingException("can't load " + node, e);
        }
    }

    private void markAllFullyLoaded(Node node) {
        node.getAttrs().put(Loader.FULLY_LOADED, String.valueOf(true));
        node.getAttrs().put(Loader.LAZILY_LOADED, String.valueOf(true));
        node.getAttrs().put(Loader.ELEMENT_LOADED, String.valueOf(true));
        node.getChildren().forEach(this::markAllFullyLoaded);
    }

    @FunctionalInterface
    private interface LoadingInterface {
        Node load(Node node, Loader loader) throws SQLException;
    }
}
