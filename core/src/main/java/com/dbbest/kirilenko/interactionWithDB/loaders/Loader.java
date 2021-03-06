package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.tree.Node;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Loader {

    public final static String ELEMENT_LOADED = "elementLoaded";
    public final static String FULLY_LOADED = "fullyLoaded";
    public final static String LAZILY_LOADED = "lazilyLoaded";

    private Connection connection;

    public Loader() {
    }

    public Loader(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Fully loads node (fill it's attributes)
     *
     * @param node for attribute loading
     * @return loaded node
     */
    public abstract Node loadElement(Node node) throws SQLException;

    /**
     * Loads children for current node
     *
     * @param node for children loading
     * @return node with children
     */
    public abstract Node lazyChildrenLoad(Node node) throws SQLException;

    /**
     * Loads attributes for node and loads it's children
     *
     * @param node for full loading
     * @return fully loaded node
     */
    public abstract Node fullLoadElement(Node node) throws SQLException;

    /**
     * returns a list of nodes of a certain category
     *
     * @param node contains required parameters
     * @return List of Nodes
     */
    public abstract Node loadCategory(Node node) throws SQLException;

    protected abstract Node fullLoadCategory(Node node) throws SQLException;

    protected void markElementLoaded(Node node) {
        node.getAttrs().put(ELEMENT_LOADED, String.valueOf(true));
    }

    protected void markElementFullyLoaded(Node node) {
        node.getAttrs().put(FULLY_LOADED, String.valueOf(true));
    }

    protected void markElementLazilyLoaded(Node node) {
        node.getAttrs().put(LAZILY_LOADED, String.valueOf(true));
    }
    /**
     * Executes sql query with params
     *
     * @param query  - sql query
     * @param params - varargs of String parameters for prepared statement
     * @return ResultSet
     */
    protected ResultSet executeQuery(String query, String... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        return statement.executeQuery();
    }

    /**
     * Creates map from resultSet row
     *
     * @param resultSet row of resultSet
     * @return Map where key - column name, value - column value
     */
    protected Map<String, String> fillAttributes(ResultSet resultSet) throws SQLException {
        Map<String, String> attrs = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = metaData.getColumnName(i);
            String value = String.valueOf(resultSet.getObject(i));
            if ("null".equals(value) || "".equals(value)) {
                continue;
            }
            attrs.put(key, value);
        }
        return attrs;
    }
}
