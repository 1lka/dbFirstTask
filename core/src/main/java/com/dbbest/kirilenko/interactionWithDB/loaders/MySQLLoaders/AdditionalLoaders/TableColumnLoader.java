package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.Map;

public class TableColumnLoader extends AdditionalLoader {

//    private static final String SQL_QUERY =
//            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
//                    "where TABLE_SCHEMA = ? and TABLE_NAME not in " +
//                    "(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = ?) order by TABLE_NAME";

    private static final String LOAD_ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TableColumnLoader() {
    }

    public TableColumnLoader(Connection connection) {
        super(connection);
    }

    @Override
    public void loadDetails(Node node) throws SQLException {
        Node columns = new Node(MySQLConstants.NodeNames.COLUMNS);
        node.addChild(columns);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node column = new Node(MySQLConstants.DBEntity.COLUMN);
            Map<String, String> attrs = fillAttributes(resultSet);
            column.setAttrs(attrs);
            columns.addChild(column);
        }
    }
}
