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


@EntityLoader(element = {MySQLConstants.DBEntity.INDEX, MySQLConstants.NodeNames.INDEXES})
public class TableIndexLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "select TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, INDEX_NAME ,group_concat(COLUMN_NAME separator ', ') " +
                    "as COLUMNS_NAME, CARDINALITY, SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT " +
                    " from INFORMATION_SCHEMA.STATISTICS where TABLE_SCHEMA = ? and TABLE_NAME = ? and INDEX_NAME = ? group by INDEX_NAME";

    private static final String LOAD_CATEGORY_QUERY =
            "select INDEX_NAME from INFORMATION_SCHEMA.STATISTICS " +
                    "where TABLE_SCHEMA = ? and TABLE_NAME = ? and INDEX_NAME != 'PRIMARY' group by INDEX_NAME";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "select TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, INDEX_NAME ,group_concat(COLUMN_NAME separator ', ') " +
                    "as COLUMNS_NAME, CARDINALITY, SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT " +
                    " from INFORMATION_SCHEMA.STATISTICS where TABLE_SCHEMA = ? and TABLE_NAME = ? and INDEX_NAME != 'PRIMARY' group by INDEX_NAME";




    public TableIndexLoader() {
    }

    public TableIndexLoader(Connection connection) {
        super(connection);
    }

    public void load(Node tables, Connection connection) throws SQLException {
        setConnection(connection);
        for (Node table : tables.getChildren()) {
            fullLoadCategory(table);
        }
    }



    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.INDEXES.equals(node.getName())) {
            return node;
        }
        String indexName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName, indexName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.INDEX_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            markElementLoaded(node);
            return node;
        }
        throw new LoadingException("cant load index " + indexName + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.INDEX.equals(nodeName)) {
            loadElement(node);
            markElementFullyLoaded(node);
        } else {
            Node columns = findIndexes(node);
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

        Node indexes = findIndexes(table);
        if (indexes == null) {
            indexes = new Node(MySQLConstants.NodeNames.INDEXES);
            indexes.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.INDEXES);
            table.addChild(indexes);
        }
        indexes.getChildren().clear();
        List<Node> indexList = new ArrayList<>();

        while (resultSet.next()) {
            Node index = new Node(MySQLConstants.DBEntity.INDEX);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.INDEX_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            index.setAttrs(attrs);
            indexList.add(index);
        }
        indexes.addChildren(indexList);
        return table;
    }

    private Node findIndexes(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.INDEXES);
        if (nodeForLoading == null) {
            Node columns = new Node(MySQLConstants.NodeNames.INDEXES);
            columns.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.INDEXES);
            node.addChild(columns);
            nodeForLoading = columns;
        }
        return nodeForLoading;
    }
}
