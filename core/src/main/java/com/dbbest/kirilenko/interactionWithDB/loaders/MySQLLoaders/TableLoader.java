package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.*;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.TABLE, MySQLConstants.NodeNames.TABLES})
public class TableLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' and TABLE_NAME = ?";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' ";


    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE'";

    public TableLoader() {
    }

    public TableLoader(Connection connection) {
        super(connection);
    }

    /**
     * Loads table attributes for node
     *
     * @param node for attribute loading
     * @return loaded table node
     */
    @Override
    public Node loadElement(Node node) throws SQLException {
        if (tablesCheck(node)) {
            return node;
        }
        String tableName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TABLE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            markElementLoaded(node);
            return node;
        }
        throw new LoadingException("cant load " + tableName + " table in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        if (tablesCheck(node)) {
            return node;
        }
        Loader columnLoader = new TableColumnLoader(getConnection());
        Loader indexLoader = new TableIndexLoader(getConnection());
        Loader FKLoader = new TableForeignKeyLoader(getConnection());
        Loader PKLoader = new TablePrimaryKeyLoader(getConnection());
        Loader triggerLoader = new TableTriggerLoader(getConnection());

        columnLoader.loadCategory(node);
        indexLoader.loadCategory(node);
        FKLoader.loadCategory(node);
        PKLoader.loadCategory(node);
        triggerLoader.loadCategory(node);

        markElementLazilyLoaded(node);
        return node;
    }

    /**
     * Loads all tables if node is schema or
     * loads table if node is table.
     *
     * @param node schema or table
     * @return fully loaded tables
     */
    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.TABLE.equals(nodeName)) {
            if (!Boolean.valueOf(node.getAttrs().get(Loader.ELEMENT_LOADED))) {
                loadElement(node);
            }

            Loader columnLoader = new TableColumnLoader(getConnection());
            Loader indexLoader = new TableIndexLoader(getConnection());
            Loader FKLoader = new TableForeignKeyLoader(getConnection());
            Loader PKLoader = new TablePrimaryKeyLoader(getConnection());
            Loader triggerLoader = new TableTriggerLoader(getConnection());

            columnLoader.fullLoadElement(node);
            indexLoader.fullLoadElement(node);
            FKLoader.fullLoadElement(node);
            PKLoader.fullLoadElement(node);
            triggerLoader.fullLoadElement(node);

            markElementFullyLoaded(node);
        } else {
            Node tables = findTables(node);
            fullLoadCategory(tables);
            for (Node table : tables.getChildren()) {
                markElementLoaded(table);
                fullLoadElement(table);
            }
        }
        return node;
    }

    /**
     * loads all table names
     *
     * @param node contains required parameters
     * @return list of loaded tables
     */
    @Override
    public Node loadCategory(Node node) throws SQLException {
        Node nodeForLoading = findTables(node);
        List<Node> tables = loadAll(LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    @Override
    public Node fullLoadCategory(Node node) throws SQLException {
        Node nodeForLoading = findTables(node);
        List<Node> tables = loadAll(FULL_LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    private boolean tablesCheck(Node node) {
        return MySQLConstants.NodeNames.TABLES.equals(node.getName());
    }

    private Node findTables(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.TABLES);
        if (nodeForLoading == null) {
            Node tables = new Node(MySQLConstants.NodeNames.TABLES);
            tables.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.TABLES);
            node.addChild(tables);
            nodeForLoading = tables;
        }
        nodeForLoading.getChildren().clear();
        return nodeForLoading;
    }

    private List<Node> loadAll(String query, Node tablesNode) throws SQLException {
        List<Node> tables = new ChildrenList<>();
        String schemaName = tablesNode.getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName);
        while (resultSet.next()) {
            Node table = new Node(MySQLConstants.DBEntity.TABLE);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TABLE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            table.setAttrs(attrs);
            tables.add(table);
        }
        return tables;
    }
}
