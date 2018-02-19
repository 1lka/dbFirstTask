package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class ForeignKeyLoader implements AdditionalLoader {

    private static final String SQL_QUERY =
            "select A.CONSTRAINT_CATALOG, A.CONSTRAINT_SCHEMA, A.CONSTRAINT_NAME, A.UNIQUE_CONSTRAINT_CATALOG, " +
                    "A.UNIQUE_CONSTRAINT_NAME, A.MATCH_OPTION, A.UPDATE_RULE, A.DELETE_RULE, A.TABLE_NAME, " +
                    "A.REFERENCED_TABLE_NAME, B.COLUMN_NAME, B.REFERENCED_COLUMN_NAME, B.ORDINAL_POSITION, B.POSITION_IN_UNIQUE_CONSTRAINT " +
                    "from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A " +
                    "left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B " +
                    "on A.constraint_name = B.constraint_name where A.CONSTRAINT_SCHEMA = ?";


    @Override
    public void load(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);

        ResultSet rs = ps.executeQuery();
        for (Node table : tables.getChildren()) {
            fillTableFKeys(table, rs);
        }
    }

    private void fillTableFKeys(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node foreignKeys = new Node(DBElement.FOREIGN_KEYS);
        table.addChild(foreignKeys);

        String name = table.getAttrs().get(TABLE_NAME);

        while (rs.next()) {
            String tableName = rs.getString(TABLE_NAME);
            if (tableName.equals(name)) {
                Node fKey = new Node(DBElement.FOREIGN_KEY);
                Map<String, String> attrs = fKey.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = String.valueOf(rs.getObject(i));
                    attrs.put(columnName, columnValue);
                }
                foreignKeys.addChild(fKey);
            } else {
                rs.previous();
                return;
            }
        }
    }
}
