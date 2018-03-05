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

public class TableColumnLoader extends Loader {

//    private static final String SQL_QUERY =
//            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
//                    "where TABLE_SCHEMA = ? and TABLE_NAME not in " +
//                    "(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = ?) order by TABLE_NAME";

    private static final String LOAD_ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TableColumnLoader() {
    }

    public TableColumnLoader(Connection connection) {
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
        String tableName = table.getAttrs().get(MySQLConstants.AttributeName.TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        List<Node> columnList = new ChildrenList<>();
        while (resultSet.next()) {
            Node column = new Node(MySQLConstants.DBEntity.COLUMN);
            Map<String, String> attrs = fillAttributes(resultSet);
            column.setAttrs(attrs);
            columnList.add(column);
        }
        return columnList;
    }
}
