package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.SCHEMA)
public class SchemaLoader extends Loader {

    private static final String CATALOG_NAME = "CATALOG_NAME";
    private static final String DEFAULT_CHARACTER_SET_NAME = "DEFAULT_CHARACTER_SET_NAME";
    private static final String DEFAULT_COLLATION_NAME = "DEFAULT_COLLATION_NAME";
    private static final String SQL_PATH = "SQL_PATH";
    private static final String SCHEMA_NAME = "SCHEMA_NAME";

    private static final String SQL_QUERY =
            "SELECT CATALOG_NAME,DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME,SQL_PATH " +
                    "FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";


    @Override
    public final Node load(Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement statement = connection.prepareStatement(SQL_QUERY);
        statement.setString(1, schema);
        ResultSet rs = statement.executeQuery();

        int i = 1;
        rs.next();
        Node node = new Node(DBElement.SCHEMA.toString());
        Map<String, String> attrs = node.getAttrs();
        attrs.put(CATALOG_NAME, rs.getString(i++));
        attrs.put(SCHEMA_NAME, schema);
        attrs.put(DEFAULT_CHARACTER_SET_NAME, rs.getString(i++));
        attrs.put(DEFAULT_COLLATION_NAME, rs.getString(i++));
        attrs.put(SQL_PATH, rs.getString(i));

        rs.close();

        return node;
    }
}
