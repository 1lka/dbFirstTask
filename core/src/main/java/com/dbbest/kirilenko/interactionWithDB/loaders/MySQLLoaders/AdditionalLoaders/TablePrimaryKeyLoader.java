package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class TablePrimaryKeyLoader extends Loader {

//    private static final String SQL_QUERY =
//            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
//                    "where CONSTRAINT_SCHEMA = ? and CONSTRAINT_NAME = 'PRIMARY' order by TABLE_NAME";

    private static final String LOAD_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_NAME = 'PRIMARY' and TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TablePrimaryKeyLoader() {
    }

    public TablePrimaryKeyLoader(Connection connection) {
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
        String schemaName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_SCHEMA);
        String tableName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        List<Node> PKeys = new ChildrenList<>();

        while (resultSet.next()) {
            Node pKey = new Node(MySQLConstants.DBEntity.PRIMARY_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);
            pKey.setAttrs(attrs);
            PKeys.add(pKey);
        }
        return PKeys;
    }
}
