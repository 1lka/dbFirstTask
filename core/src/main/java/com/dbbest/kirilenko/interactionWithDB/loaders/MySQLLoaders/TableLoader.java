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

@EntityLoader(element = MySQLConstants.DBEntity.TABLE)
public class TableLoader extends Loader {

    private static final String All_TABLES_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' order by TABLE_NAME";

    private static final String TABLE_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' and TABLE_NAME = ?";

    public TableLoader() {
    }

    public TableLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    /**
     * Loads table attributes for node
     *
     * @param node for attribute loading
     * @return loaded table node
     */
    @Override
    public Node loadElement(Node node) throws SQLException {
        String tableName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_NAME);
        String schemaName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_SCHEMA);
        ResultSet resultSet = executeQuery(TABLE_QUERY, schemaName, tableName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            node.setAttrs(attrs);
            return node;
        }
        throw new LoadingException("cant load " + tableName + " table in " + schemaName + " schema");
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
        if (MySQLConstants.DBEntity.TABLE.equals(node.getName())) {
            this.loadElement(node);

            Loader columnLoader = new TableColumnLoader(getConnection());
            List<Node> columnsList = columnLoader.loadCategory(node);
            Node columns = new Node(MySQLConstants.NodeNames.COLUMNS);
            columns.addChildren(columnsList);
            node.addChild(columns);

            Loader indexLoader = new TableIndexLoader(getConnection());
            List<Node> indexesList = indexLoader.loadCategory(node);
            Node indexes = new Node(MySQLConstants.NodeNames.INDEXES);
            indexes.addChildren(indexesList);
            node.addChild(indexes);

            Loader FKLoader = new TableForeignKeyLoader(getConnection());
            List<Node> FKList = FKLoader.loadCategory(node);
            Node FKs = new Node(MySQLConstants.NodeNames.FOREIGN_KEYS);
            FKs.addChildren(FKList);
            node.addChild(FKs);

            Loader PKLoader = new TablePrimaryKeyLoader(getConnection());
            List<Node> PKList = PKLoader.loadCategory(node);
            Node PKs = new Node(MySQLConstants.NodeNames.PRIMARY_KEYS);
            PKs.addChildren(PKList);
            node.addChild(PKs);

            Loader triggerLoader = new TableTriggerLoader(getConnection());
            List<Node> triggersList = triggerLoader.loadCategory(node);
            Node triggers = new Node(MySQLConstants.NodeNames.TRIGGERS);
            triggers.addChildren(triggersList);
            node.addChild(triggers);

            return node;
            //todo delete
        } else if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            Node tables = node.wideSearch(MySQLConstants.NodeNames.TABLES);
            List<Node> tablesList;
            if (tables == null) {
                tables = new Node(MySQLConstants.NodeNames.TABLES);
                node.addChild(tables);
                tablesList = loadCategory(node);
                tables.addChildren(tablesList);
            } else {
                tablesList = tables.getChildren();
            }
            for (Node table : tablesList) {
                fullLoadElement(table);
            }
        }
        return node;
    }

    /**
     * loads all tables with loaded attributes
     *
     * @param node contains required parameters
     * @return list of loaded tables
     */
    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> tables = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.SCHEMA_NAME);
        ResultSet resultSet = executeQuery(All_TABLES_QUERY, schema);
        while (resultSet.next()) {
            Node table = new Node(MySQLConstants.DBEntity.TABLE);
            Map<String, String> attrs = fillAttributes(resultSet);
            table.setAttrs(attrs);
            tables.add(table);
        }
        return tables;
    }
}
