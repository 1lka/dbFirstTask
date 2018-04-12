package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.COLUMN, MySQLConstants.NodeNames.COLUMNS})
public class TableColumnLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ? and COLUMN_NAME = ?";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TableColumnLoader() {
    }

    public TableColumnLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.COLUMNS.equals(node.getName())) {
            return node;
        }
        String columnName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName, columnName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        }
        throw new LoadingException("cant load parameter " + columnName + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.COLUMN.equals(nodeName)) {
            loadElement(node);
        } else {
            Node columns = findColumns(node);
            fullLoadCategory(columns.getParent());
        }
        return node;
    }

    @Override
    public Node loadCategory(Node table) throws SQLException {
        return loadAll(LOAD_CATEGORY_QUERY, table);
    }

    @Override
    public Node fullLoadCategory(Node table) throws SQLException {
        return loadAll(FULL_LOAD_CATEGORY_QUERY, table);
    }

    private Node loadAll(String query, Node table) throws SQLException {
        String schemaName = table.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = table.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName, tableName);

        Node columns = findColumns(table);
        if (columns == null) {
            columns = new Node(MySQLConstants.NodeNames.COLUMNS);
            table.addChild(columns);
        }
        columns.getChildren().clear();
        List<Node> columnList = new ArrayList<>();

        while (resultSet.next()) {
            Node column = new Node(MySQLConstants.DBEntity.COLUMN);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            column.setAttrs(attrs);
            columnList.add(column);
        }
        columns.addChildren(columnList);
        return table;
    }

    private Node findColumns(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.COLUMNS);
        if (nodeForLoading == null) {
            Node columns = new Node(MySQLConstants.NodeNames.COLUMNS);
            node.addChild(columns);
            nodeForLoading = columns;
        }
        return nodeForLoading;
    }
}
