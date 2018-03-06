package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.List;
import java.util.Map;

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
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node loadElement(Node node) {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        Node views = new Node(MySQLConstants.NodeNames.VIEWS);
        List<Node> viewList = loadCategory(node);
        views.addChildren(viewList);
        node.addChild(views);
        return node;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> viewList = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.SCHEMA_NAME);
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node view = new Node(MySQLConstants.DBEntity.VIEW);
            Map<String, String> attrs = fillAttributes(resultSet);
            view.setAttrs(attrs);
            viewList.add(view);
        }
        return viewList;
    }
}
