package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@EntityLoader(element = {MySQLConstants.DBEntity.TRIGGER, MySQLConstants.NodeNames.TRIGGERS})
public class TableTriggerLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ? and EVENT_OBJECT_TABLE = ? and TRIGGER_NAME = ?";

    private static final String LOAD_CATEGORY_QUERY =
            "select TRIGGER_NAME from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ? and EVENT_OBJECT_TABLE = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "select * from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ? and EVENT_OBJECT_TABLE = ?";

    public TableTriggerLoader() {
    }

    public TableTriggerLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.TRIGGERS.equals(node.getName())) {
            return node;
        }
        String triggerName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName, triggerName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TRIGGER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        }
        throw new LoadingException("cant load trigger " + triggerName + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.TRIGGER.equals(nodeName)) {
            loadElement(node);
        } else {
            Node triggers = findTriggers(node);
            fullLoadCategory(triggers.getParent());
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

        Node triggers = findTriggers(table);
        if (triggers == null) {
            triggers = new Node(MySQLConstants.NodeNames.TRIGGERS);
            table.addChild(triggers);
        }
        triggers.getChildren().clear();
        List<Node> triggerList = new ArrayList<>();

        while (resultSet.next()) {
            Node trgger = new Node(MySQLConstants.DBEntity.TRIGGER);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.TRIGGER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            trgger.setAttrs(attrs);
            triggerList.add(trgger);
        }
        triggers.addChildren(triggerList);
        return table;
    }

    private Node findTriggers(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.TRIGGERS);
        if (nodeForLoading == null) {
            Node triggers = new Node(MySQLConstants.NodeNames.TRIGGERS);
            node.addChild(triggers);
            nodeForLoading = triggers;
        }
        return nodeForLoading;
    }
}
