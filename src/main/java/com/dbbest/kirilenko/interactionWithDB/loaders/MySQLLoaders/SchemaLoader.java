package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.SCHEMA)
public class SchemaLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

    @Override
    public final void lazyLoad(Node node, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement statement = connection.prepareStatement(SQL_QUERY);
        statement.setString(1, schema);
        ResultSet rs = statement.executeQuery();

        rs.next();
        node.setName(DBElement.SCHEMA);
        Map<String, String> attrs = node.getAttrs();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            String key = rsmd.getColumnName(i);
            String value = (String) rs.getObject(i);
            attrs.put(key, value);
        }
        rs.close();
    }
}