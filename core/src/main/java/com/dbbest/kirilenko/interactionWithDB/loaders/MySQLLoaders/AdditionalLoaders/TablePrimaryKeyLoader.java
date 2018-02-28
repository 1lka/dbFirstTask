package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.Map;

public class TablePrimaryKeyLoader extends AdditionalLoader {

//    private static final String SQL_QUERY =
//            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
//                    "where CONSTRAINT_SCHEMA = ? and CONSTRAINT_NAME = 'PRIMARY' order by TABLE_NAME";

    private static final String LOAD_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_NAME = 'PRIMARY' and TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TablePrimaryKeyLoader() {
    }

    public TablePrimaryKeyLoader(Connection connection) {
        super(connection);
    }

    @Override
    public void loadDetails(Node node) throws SQLException {
        Node pKeys = new Node(MySQLConstants.NodeNames.PRIMARY_KEYS);
        node.addChild(pKeys);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node pKey = new Node(MySQLConstants.DBEntity.PRIMARY_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);
            pKey.setAttrs(attrs);
            pKeys.addChild(pKey);
        }
    }
}
