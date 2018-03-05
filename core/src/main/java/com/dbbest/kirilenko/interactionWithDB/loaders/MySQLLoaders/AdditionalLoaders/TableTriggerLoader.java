package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class TableTriggerLoader extends Loader {

//    private static final String SQL_QUERY =
//            "select * from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ?";

    private static final String LOAD_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = ? and EVENT_OBJECT_TABLE = ?";

    public TableTriggerLoader() {
    }

    public TableTriggerLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node fullLoadElement(Node node) {
        return null;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        return null;
    }


    public void loadDetails(Node node) throws SQLException {
        Node triggers = new Node(MySQLConstants.NodeNames.TRIGGERS);
        node.addChild(triggers);

        String schemaName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_SCHEMA);
        String tableName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node trigger = new Node(MySQLConstants.DBEntity.TRIGGER);
            Map<String, String> attrs = fillAttributes(resultSet);
            trigger.setAttrs(attrs);
            triggers.addChild(trigger);
        }
    }
}
