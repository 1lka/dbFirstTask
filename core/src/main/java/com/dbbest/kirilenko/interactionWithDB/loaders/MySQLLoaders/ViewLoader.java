package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.VIEW, MySQLConstants.NodeNames.VIEWS})
public class ViewLoader extends Loader {

    private static final String SQL_LAZY_QUERY =
            "SELECT TABLE_NAME, TABLE_SCHEMA FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    private static final String SQL_FULL_ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    private static final String SQL_FULL_VIEWS_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    public ViewLoader() {
    }

    public ViewLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) {
        return node;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        String viewName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_SCHEMA);
        ResultSet resultSet = executeQuery(SQL_FULL_ELEMENT_QUERY, schema,viewName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such view: " + viewName);
        }
}

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.VIEW.equals(node.getName())) {
            return loadElement(node);
        }
        if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            Node views = node.wideSearch(MySQLConstants.NodeNames.VIEWS);
            List<Node> viewsList;
            if (views == null) {
                views = new Node(MySQLConstants.NodeNames.VIEWS);
                node.addChild(views);
                viewsList = new ChildrenList<>();
                String schema = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
                ResultSet resultSet = executeQuery(SQL_FULL_VIEWS_QUERY, schema);
                while (resultSet.next()) {
                    Node view = new Node(MySQLConstants.DBEntity.VIEW);
                    Map<String, String> attrs = fillAttributes(resultSet);
                    String name = attrs.remove(MySQLConstants.AttributeName.TABLE_NAME);
                    attrs.put(MySQLConstants.AttributeName.NAME, name);
                    view.setAttrs(attrs);
                    viewsList.add(view);
                }
                views.addChildren(viewsList);
            } else {
                viewsList = views.getChildren();
            }
            for (Node view : viewsList) {
                fullLoadElement(view);
            }
            views.getAttrs().put("childrenCount", String.valueOf(viewsList.size()));
        }
        return node;
    }

    /**
     * loads all views
     *
     * @param node contains required parameters
     * @return list of views
     */
    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> viewList = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(SQL_LAZY_QUERY, schema);
        while (resultSet.next()) {
            Node view = new Node(MySQLConstants.DBEntity.VIEW);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TABLE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            view.setAttrs(attrs);
            viewList.add(view);
        }
        return viewList;
    }
}
