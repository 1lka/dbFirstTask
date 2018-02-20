package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Load(element = DBElement.TABLE, parent = SchemaLoader.class)
public class TableLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' order by TABLE_NAME";

    @Override
    public Node lazyLoad(String schema) throws SQLException {
        Node tables = new Node(DBElement.TABLES);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node table = new Node(DBElement.TABLE);
            table.setAttrs(fillAttributes(resultSet));
            tables.addChild(table);
        }
        return tables;
    }

}
