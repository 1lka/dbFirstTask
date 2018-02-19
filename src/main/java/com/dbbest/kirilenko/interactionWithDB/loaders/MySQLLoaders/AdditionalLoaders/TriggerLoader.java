package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class TriggerLoader implements AdditionalLoader {

    private static final String SQL_QUERY =
            "select * from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ?";

    @Override
    public void load(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);

        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTableTriggers(table, rs);
        }

    }

    private void fillTableTriggers(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node triggers = new Node(DBElement.TRIGGERS);
        table.addChild(triggers);

        String name = table.getAttrs().get(TABLE_NAME);
        while (rs.next()) {
            String tableName = rs.getString("EVENT_OBJECT_TABLE");
            if (tableName.equals(name)) {
                Node trigger = new Node(DBElement.TRIGGER);
                Map<String, String> attrs = trigger.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = String.valueOf(rs.getObject(i));
                    attrs.put(columnName, columnValue);
                }
                triggers.addChild(trigger);
            } else {
                rs.previous();
                return;
            }
        }
    }
}
