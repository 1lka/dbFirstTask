package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants.AttributeName.TABLE_NAME;
import static com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants.AttributeName.TABLE_SCHEMA;

public class TableIndexLoader extends Loader {

    private static final String LOAD_ELEMENT_QUERY =
            "select TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, INDEX_NAME ,group_concat(COLUMN_NAME separator ', ') " +
                    "as COLUMNS_NAME, CARDINALITY, SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT " +
                    " from INFORMATION_SCHEMA.STATISTICS where TABLE_SCHEMA = ? and TABLE_NAME = ? and INDEX_NAME != 'PRIMARY' group by INDEX_NAME";

    public TableIndexLoader() {
    }

    public TableIndexLoader(Connection connection) {
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

        List<Node> indexesList = new ChildrenList<>();
        while (resultSet.next()) {
            Node index = new Node(MySQLConstants.DBEntity.INDEX);
            Map<String, String> attrs = fillAttributes(resultSet);
            index.setAttrs(attrs);
            indexesList.add(index);
        }
        return indexesList;
    }

    public void loadDetails(Node node) throws SQLException {
        Node indexes = new Node(MySQLConstants.NodeNames.INDEXES);
        node.addChild(indexes);

        String schemaName = node.getAttrs().get(TABLE_SCHEMA);
        String tableName = node.getAttrs().get(TABLE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, tableName);

        while (resultSet.next()) {
            Node index = new Node(MySQLConstants.DBEntity.INDEX);
            Map<String, String> attrs = fillAttributes(resultSet);
            index.setAttrs(attrs);
            indexes.addChild(index);
        }
    }
}
