package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.tree.Node;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Loader {

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
     * Loads children for current node
     *
     * @param node for children loading
     * @return node with children
     * @throws SQLException
     */
    public abstract Node lazyChildrenLoad(Node node) throws SQLException;

    /**
     * Fully loads node (fill it's attributes)
     *
     * @param node
     * @return
     * @throws SQLException
     */
    public abstract Node loadElement(Node node) throws SQLException;

    /**
     * Loads attributes for node and loads it's children
     *
     * @param node
     * @return
     */
    public abstract Node fullLoad(Node node) throws SQLException;

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

    protected ResultSet executeQuery(String query, String... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        return statement.executeQuery();
    }


}
