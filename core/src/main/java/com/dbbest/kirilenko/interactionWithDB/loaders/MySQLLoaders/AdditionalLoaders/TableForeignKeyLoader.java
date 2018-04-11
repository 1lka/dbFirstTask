package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TableForeignKeyLoader extends Loader {

    private static final String LOAD_ELEMENT_QUERY =
            "select A.CONSTRAINT_NAME from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ?";
//private static final String LOAD_ELEMENT_QUERY =
//            "select A.CONSTRAINT_CATALOG, A.CONSTRAINT_SCHEMA, A.CONSTRAINT_NAME, A.UNIQUE_CONSTRAINT_CATALOG," +
//                    " A.UNIQUE_CONSTRAINT_NAME, A.MATCH_OPTION, A.UPDATE_RULE, A.DELETE_RULE, A.TABLE_NAME," +
//                    " A.REFERENCED_TABLE_NAME, B.COLUMN_NAME, B.REFERENCED_COLUMN_NAME, B.ORDINAL_POSITION," +
//                    " B.POSITION_IN_UNIQUE_CONSTRAINT from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS as A" +
//                    " left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as B on A.constraint_name = B.constraint_name" +
//                    " where A.CONSTRAINT_SCHEMA = ? and A.TABLE_NAME = ?";


    public TableForeignKeyLoader() {
    }

    public TableForeignKeyLoader(Connection connection) {
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
    public List<Node> loadCategory(Node table) throws SQLException {
        String schemaName = table.getAttrs().get(MySQLConstants.AttributeName.TABLE_SCHEMA);
        String tableName = table.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        List<Node> FKList = new ChildrenList<>();
        while (resultSet.next()) {
            Node FKey = new Node(MySQLConstants.DBEntity.FOREIGN_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);

            String name = attrs.remove(MySQLConstants.AttributeName.CONSTRAINT_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);

            FKey.setAttrs(attrs);
            FKList.add(FKey);
        }
        return FKList;
    }


}
