package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class PrimaryKeyLoader implements AdditionalLoader {

    private static final String SQL_QUERY =
            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_SCHEMA = ? and CONSTRAINT_NAME = 'PRIMARY' order by TABLE_NAME";


    @Override
    public void load(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);
        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTablePK(table, rs);
        }

    }

    private void fillTablePK(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node pks = new Node(DBElement.PRIMARY_KEYS);
        table.addChild(pks);

        String name = table.getAttrs().get(TABLE_NAME);

        while (rs.next()) {
            String tableName = rs.getString(TABLE_NAME);
            if (tableName.equals(name)) {
                Node pk = new Node(DBElement.PRIMARY_KEY);
                Map<String, String> attrs = pk.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String indexName = rsmd.getColumnName(i);
                    String indexValue = String.valueOf(rs.getObject(i));
                    attrs.put(indexName, indexValue);
                }
                pks.addChild(pk);
            } else {
                rs.previous();
                return;
            }
        }
    }
}
