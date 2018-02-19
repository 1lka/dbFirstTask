package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class ColumnLoader implements AdditionalLoader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
                    "where TABLE_SCHEMA = ? and TABLE_NAME not in " +
                    "(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = ?) order by TABLE_NAME";

    public void load(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);
        ps.setString(2, schema);
        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTableColumns(table, rs);
        }
    }

    private void fillTableColumns(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node columns = new Node(DBElement.COLUMNS);
        table.addChild(columns);

        String name = table.getAttrs().get(TABLE_NAME);
        while (rs.next()) {
            String tableName = rs.getString(TABLE_NAME);
            if (tableName.equals(name)) {
                Node column = new Node(DBElement.COLUMN);
                Map<String, String> attrs = column.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = String.valueOf(rs.getObject(i));
                    attrs.put(columnName, columnValue);
                }
                columns.addChild(column);
            } else {
                rs.previous();
                return;
            }
        }
    }


}
