package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;

import java.sql.*;
import java.util.Map;

public class IndexLoader implements AdditionalLoader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.STATISTICS " +
            "where TABLE_SCHEMA = ? order by TABLE_NAME";



    @Override
    public void load(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);
        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTableIndex(table, rs);
        }
    }

    private void fillTableIndex(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node indexes = new Node(DBElement.INDEXES);
        table.addChild(indexes);

        String name = table.getAttrs().get(TABLE_NAME);

        while (rs.next()) {
            String tableName = rs.getString(TABLE_NAME);
            if (tableName.equals(name)) {
                Node index = new Node(DBElement.INDEX);
                Map<String, String> attrs = index.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String indexName = rsmd.getColumnName(i);
                    String indexValue = String.valueOf(rs.getObject(i));
                    attrs.put(indexName, indexValue);
                }
                indexes.addChild(index);
            } else {
                rs.previous();
                return;
            }
        }
    }
}
