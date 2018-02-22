package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class TableForeignKeyLoader extends AdditionalLoader {

    private static final String LOAD_ELEMENT_QUERY =
            "select A.CONSTRAINT_CATALOG, A.CONSTRAINT_SCHEMA, A.CONSTRAINT_NAME, A.UNIQUE_CONSTRAINT_CATALOG," +
                    " A.UNIQUE_CONSTRAINT_NAME, A.MATCH_OPTION, A.UPDATE_RULE, A.DELETE_RULE, A.TABLE_NAME," +
                    " A.REFERENCED_TABLE_NAME, B.COLUMN_NAME, B.REFERENCED_COLUMN_NAME, B.ORDINAL_POSITION," +
                    " B.POSITION_IN_UNIQUE_CONSTRAINT from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ?";


    public TableForeignKeyLoader() {
    }

    public TableForeignKeyLoader(Connection connection) {
        super(connection);
    }

    @Override
    public void loadDetails(Node node) throws SQLException {
        Node fKeys = new Node(DBElement.FOREIGN_KEYS);
        node.addChild(fKeys);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node column = new Node(DBElement.FOREIGN_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);
            column.setAttrs(attrs);
            fKeys.addChild(column);
        }
    }
}
