package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;

import java.sql.*;
import java.util.Map;

public class TableIndexLoader extends AdditionalLoader {

    private static final String LOAD_ELEMENT_QUERY =
            "select TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, INDEX_NAME ,group_concat(COLUMN_NAME separator ', ') " +
                    "as COLUMNS_NAME, CARDINALITY, SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT " +
                    " from INFORMATION_SCHEMA.STATISTICS where TABLE_SCHEMA = ? and TABLE_NAME = ? and INDEX_NAME != 'PRIMARY' group by INDEX_NAME";

    public TableIndexLoader() {
    }

    public TableIndexLoader(Connection connection) {
        super(connection);
    }

    @Override
    public void loadDetails(Node node) throws SQLException {
        Node indexes = new Node(DBElement.INDEXES);
        node.addChild(indexes);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node index = new Node(DBElement.INDEX);
            Map<String, String> attrs = fillAttributes(resultSet);
            index.setAttrs(attrs);
            indexes.addChild(index);
        }
    }
}
