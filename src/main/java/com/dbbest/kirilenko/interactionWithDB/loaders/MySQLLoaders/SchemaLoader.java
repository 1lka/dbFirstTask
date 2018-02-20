package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Load(element = DBElement.SCHEMA)
public class SchemaLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

    @Override
    public Node lazyLoad(String schema) throws SQLException {
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        if (resultSet.next()) {
            Node schemaNode = new Node(DBElement.SCHEMA);
            Map<String, String> attrs = fillAttributes(resultSet);
            schemaNode.setAttrs(attrs);
            return schemaNode;
        } else {
            throw new RuntimeException("no such schema");
        }
    }

}
