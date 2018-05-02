package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.GeneralConstants;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.COLUMN, MySQLConstants.NodeNames.COLUMNS})
public class TableColumnLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ? and COLUMN_NAME = ? order by TABLE_NAME";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TableColumnLoader() {
    }

    public TableColumnLoader(Connection connection) {        super(connection);
    }

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
                    "where TABLE_SCHEMA = ? and TABLE_NAME not in " +
                    "(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = ?) order by TABLE_NAME";

    public void load(Node tables, Connection connection) throws SQLException {
        String schema = tables.getParent().getAttrs().get(GeneralConstants.NAME);
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);
        ps.setString(2, schema);

        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTableColumns(table, rs);
        }
    }

    private void fillTableColumns(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node columns = findColumns(table);

        String name = table.getAttrs().get(GeneralConstants.NAME);
        while (rs.next()) {
            String tableName = rs.getString(MySQLConstants.AttributeName.TABLE_NAME);
            if (tableName.equals(name)) {
                Node column = new Node(MySQLConstants.DBEntity.COLUMN);
                Map<String, String> attrs = column.getAttrs();

                for (int i = 1; i <= columnsCount; i++) {

                    String key = rsmd.getColumnName(i);
                    String value = String.valueOf(rs.getObject(i));
                    if ("null".equals(value) || "".equals(value)) {
                        continue;
                    }
                    attrs.put(key, value);

                }
                String columnName = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
                attrs.put(GeneralConstants.NAME, columnName);
                columns.addChild(column);
            } else {
                rs.previous();
                return;
            }
        }
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
            markElementLoaded(node);
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
            markElementFullyLoaded(node);
        } else {
            Node columns = findColumns(node);
            fullLoadCategory(columns.getParent());
            for (Node n : columns.getChildren()) {
                markElementFullyLoaded(n);
            }
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
            columns.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.COLUMNS);
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
            columns.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.COLUMNS);
            node.addChild(columns);
            nodeForLoading = columns;
        }
        return nodeForLoading;
    }
}
