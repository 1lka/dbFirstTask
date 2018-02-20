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
    public Node lazyLoadWithChildren(String schema) throws SQLException {
        Node views = new Node(DBElement.VIEWS);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node view = new Node(DBElement.VIEW);
            view.setAttrs(fillAttributes(resultSet));
            views.addChild(view);
        }

        return views;
    }
}
