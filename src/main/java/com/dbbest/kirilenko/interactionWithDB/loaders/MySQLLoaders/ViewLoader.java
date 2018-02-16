package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.VIEW,parent = SchemaLoader.class)
public class ViewLoader extends Loader{

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    @Override
    public void lazyLoad(Node node, Connection connection) throws SQLException {
        Node views = new Node(DBElement.VIEWS);
        node.addChild(views);

        String schema = connection.getCatalog();
        PreparedStatement statement = connection.prepareStatement(SQL_QUERY);
        statement.setString(1, schema);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        while (rs.next()) {
            Node view = new Node(DBElement.VIEW);
            Map<String, String> attrs = view.getAttrs();
            for (int i = 1; i <= columnsCount; i++) {
                String key = rsmd.getColumnName(i);
                String value = String.valueOf(rs.getObject(i));
                attrs.put(key, value);
            }
            views.addChild(view);
        }
    }

    @Override
    public void fullLoadOnLazy(Node node, Connection connection) throws SQLException {

    }

    @Override
    public Node fullLoad(Connection connection) throws SQLException {
        return null;
    }
}
