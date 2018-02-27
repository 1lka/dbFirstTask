package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AdditionalLoader {

    private Connection connection;

    static final String TABLE_NAME = "TABLE_NAME";

    static final String TABLE_SCHEMA = "TABLE_SCHEMA";

    public AdditionalLoader() {
    }

    public AdditionalLoader(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

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

    public abstract void loadDetails(Node node) throws SQLException;

}
