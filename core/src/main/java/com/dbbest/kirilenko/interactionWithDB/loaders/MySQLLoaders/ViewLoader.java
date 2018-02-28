package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;

@EntityLoader(element = MySQLConstants.DBEntity.VIEW)
public class ViewLoader extends Loader{

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    public ViewLoader() {
    }

    public ViewLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyLoad(String schema) throws SQLException {
        Node views = new Node(MySQLConstants.NodeNames.VIEWS);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node view = new Node(MySQLConstants.DBEntity.VIEW);
            view.setAttrs(fillAttributes(resultSet));
            views.addChild(view);
        }
        return views;
    }

    @Override
    public void loadElement(Node node) {
    }

}
