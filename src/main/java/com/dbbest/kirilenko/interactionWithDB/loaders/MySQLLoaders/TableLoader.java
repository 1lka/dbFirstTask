package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.TABLE, parent = SchemaLoader.class)
public class TableLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' order by TABLE_NAME";

    private static final String TABLE_NAME = "TABLE_NAME";

    @Override
    public void lazyLoad(Node node, Connection connection) throws SQLException {
        Node tables = new Node(DBElement.TABLES);
        node.addChild(tables);

        PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
        ps.setString(1, connection.getCatalog());
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        while (rs.next()) {
            Node table = new Node(DBElement.TABLE);
            Map<String, String> attrs = table.getAttrs();
            for (int i = 1; i <= columnsCount; i++) {
                String key = rsmd.getColumnName(i);
                String value = String.valueOf(rs.getObject(i));
                attrs.put(key, value);
            }
            tables.addChild(table);
        }
    }

    @Override
    public void fullLoadOnLazy(Node node, Connection connection) throws SQLException {
        Node tables = node.wideSearch(DBElement.TABLES);
        ColumnLoader columnLoader = new ColumnLoader();
        columnLoader.loadColumns(tables, connection);
    }

    @Override
    public Node fullLoad(Connection connection) throws SQLException {
        return null;
    }
}
