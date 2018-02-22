package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class TableTriggerLoader extends AdditionalLoader {

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
    public void loadDetails(Node node) throws SQLException {
        Node triggers = new Node(DBElement.TRIGGERS);
        node.addChild(triggers);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node trigger = new Node(DBElement.TRIGGER);
            Map<String, String> attrs = fillAttributes(resultSet);
            trigger.setAttrs(attrs);
            triggers.addChild(trigger);
        }
    }
}
