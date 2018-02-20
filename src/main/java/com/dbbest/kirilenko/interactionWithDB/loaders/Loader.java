package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Loader {

    private Class parent;

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Class getParent() {
        return parent;
    }

    public void setParent(Class parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return parent == null;
    }


    public abstract Node lazyLoad(String schema) throws SQLException;

    protected Map<String, String> fillAttributes(ResultSet resultSet) throws SQLException {
        Map<String, String> attrs = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String key = metaData.getColumnName(i);
            String value = String.valueOf(resultSet.getObject(i));
            attrs.put(key, value);
        }
        return attrs;
    }

    protected ResultSet executeQuery(String query, String param) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, param);
        return statement.executeQuery();
    }


}
