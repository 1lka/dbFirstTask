package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.TableColumnLoader;
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
        return null;
    }

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
     * @return
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

            //todo load indexes PKs FKs

            return node;

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
