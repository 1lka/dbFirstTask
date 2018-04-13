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

@EntityLoader(element = {MySQLConstants.DBEntity.FOREIGN_KEY, MySQLConstants.NodeNames.FOREIGN_KEYS})
public class TableForeignKeyLoader extends Loader {

    private static final String LOAD_CATEGORY_QUERY =
            "select A.CONSTRAINT_NAME from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "select A.CONSTRAINT_CATALOG, A.CONSTRAINT_SCHEMA, A.CONSTRAINT_NAME, A.UNIQUE_CONSTRAINT_CATALOG," +
                    " A.UNIQUE_CONSTRAINT_NAME, A.MATCH_OPTION, A.UPDATE_RULE, A.DELETE_RULE, A.TABLE_NAME," +
                    " A.REFERENCED_TABLE_NAME, B.COLUMN_NAME, B.REFERENCED_COLUMN_NAME, B.ORDINAL_POSITION," +
                    " B.POSITION_IN_UNIQUE_CONSTRAINT from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ?";

    private static final String ELEMENT_QUERY =
            "select A.CONSTRAINT_CATALOG, A.CONSTRAINT_SCHEMA, A.CONSTRAINT_NAME, A.UNIQUE_CONSTRAINT_CATALOG," +
                    " A.UNIQUE_CONSTRAINT_NAME, A.MATCH_OPTION, A.UPDATE_RULE, A.DELETE_RULE, A.TABLE_NAME," +
                    " A.REFERENCED_TABLE_NAME, B.COLUMN_NAME, B.REFERENCED_COLUMN_NAME, B.ORDINAL_POSITION," +
                    " B.POSITION_IN_UNIQUE_CONSTRAINT from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ? and A.CONSTRAINT_NAME = ?";

    public TableForeignKeyLoader() {
    }

    public TableForeignKeyLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.FOREIGN_KEYS.equals(node.getName())) {
            return node;
        }
        String columnName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName, columnName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.CONSTRAINT_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            markElementLoaded(node);
            return node;
        }
        throw new LoadingException("cant load foreign key " + columnName + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.FOREIGN_KEY.equals(nodeName)) {
            loadElement(node);
            markElementFullyLoaded(node);
        } else {
            Node fKeys = findFKeys(node);
            fullLoadCategory(fKeys.getParent());
            for (Node n : fKeys.getChildren()) {
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

        Node fKeys = findFKeys(table);
        if (fKeys == null) {
            fKeys = new Node(MySQLConstants.NodeNames.FOREIGN_KEYS);
            fKeys.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.FOREIGN_KEYS);
            table.addChild(fKeys);
        }
        fKeys.getChildren().clear();
        List<Node> fKeysList = new ArrayList<>();

        while (resultSet.next()) {
            Node column = new Node(MySQLConstants.DBEntity.FOREIGN_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.CONSTRAINT_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            column.setAttrs(attrs);
            fKeysList.add(column);
        }
        fKeys.addChildren(fKeysList);

        return table;
    }

    private Node findFKeys(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.FOREIGN_KEYS);
        if (nodeForLoading == null) {
            Node FKs = new Node(MySQLConstants.NodeNames.FOREIGN_KEYS);
            FKs.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.FOREIGN_KEYS);
            node.addChild(FKs);
            nodeForLoading = FKs;
        }
        nodeForLoading.getChildren().clear();
        return nodeForLoading;
    }
}
