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

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";

    public ViewLoader() {
    }

    public ViewLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        String viewName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schema = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schema, viewName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such view: " + viewName);
        }
    }

    @Override
    public Node lazyChildrenLoad(Node node) {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.VIEW.equals(node.getName())) {
            return loadElement(node);
        } else {
            Node views = findViews(node);
            fullLoadCategory(views);
        }
        return node;
    }

    @Override
    public Node fullLoadCategory(Node node) throws SQLException {
        Node nodeForLoading = findViews(node);
        List<Node> tables = loadAll(FULL_LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    /**
     * loads all views
     *
     * @param node contains required parameters
     * @return list of views
     */
    @Override
    public Node loadCategory(Node node) throws SQLException {
        Node nodeForLoading = findViews(node);
        List<Node> tables = loadAll(LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    private List<Node> loadAll(String query, Node viewsNode) throws SQLException {
        List<Node> views = new ChildrenList<>();
        String schemaName = viewsNode.getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName);
        while (resultSet.next()) {
            Node table = new Node(MySQLConstants.DBEntity.VIEW);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TABLE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            table.setAttrs(attrs);
            views.add(table);
        }
        return views;
    }

    private Node findViews(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.VIEWS);
        if (nodeForLoading == null) {
            Node tables = new Node(MySQLConstants.NodeNames.VIEWS);
            node.addChild(tables);
            nodeForLoading = tables;
        }
        nodeForLoading.getChildren().clear();
        return nodeForLoading;
    }
}
