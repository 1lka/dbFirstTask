package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.GeneralConstants;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.PRIMARY_KEY, MySQLConstants.NodeNames.PRIMARY_KEYS})
public class TablePrimaryKeyLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_NAME = 'PRIMARY' and TABLE_SCHEMA = ? and TABLE_NAME = ? and COLUMN_NAME = ?";

    private static final String LOAD_CATEGORY_QUERY =
            "select COLUMN_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_NAME = 'PRIMARY' and TABLE_SCHEMA = ? and TABLE_NAME = ?";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_NAME = 'PRIMARY' and TABLE_SCHEMA = ? and TABLE_NAME = ?";

    public TablePrimaryKeyLoader() {
    }

    public TablePrimaryKeyLoader(Connection connection) {
        super(connection);
    }

    private static final String SQL_QUERY =
            "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "where CONSTRAINT_SCHEMA = ? and CONSTRAINT_NAME = 'PRIMARY' order by TABLE_NAME";


    public void load(Node tables, Connection connection) throws SQLException {
        String schema = tables.getParent().getAttrs().get(GeneralConstants.NAME);
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);

        ResultSet rs = ps.executeQuery();

        for (Node table : tables.getChildren()) {
            fillTablePK(table, rs);
        }

    }

    private void fillTablePK(Node table, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node pks = findPrimaries(table);

        String name = table.getAttrs().get(GeneralConstants.NAME);

        while (rs.next()) {
            String tableName = rs.getString(MySQLConstants.AttributeName.TABLE_NAME);
            if (tableName.equals(name)) {
                Node pk = new Node(MySQLConstants.DBEntity.PRIMARY_KEY);
                Map<String, String> attrs = pk.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String key = rsmd.getColumnName(i);
                    String value = String.valueOf(rs.getObject(i));
                    if ("null".equals(value) || "".equals(value)) {
                        continue;
                    }
                    attrs.put(key, value);
                }
                String constr = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
                attrs.put(GeneralConstants.NAME, constr);
                pks.addChild(pk);
            } else {
                rs.previous();
                return;
            }
        }
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.PRIMARY_KEYS.equals(node.getName())) {
            return node;
        }
        String PK = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String tableName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schemaName, tableName, PK);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            markElementLoaded(node);
            return node;
        }
        throw new LoadingException("cant load primary key " + PK + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.PRIMARY_KEY.equals(nodeName)) {
            loadElement(node);
            markElementFullyLoaded(node);
        } else {
            Node columns = findPrimaries(node);
            fullLoadCategory(columns.getParent());
            for (Node n : columns.getChildren()) {
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

        Node PKs = findPrimaries(table);
        if (PKs == null) {
            PKs = new Node(MySQLConstants.NodeNames.PRIMARY_KEYS);
            PKs.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.PRIMARY_KEYS);
            table.addChild(PKs);
        }
        PKs.getChildren().clear();
        List<Node> primaryList = new ArrayList<>();

        while (resultSet.next()) {
            Node primary = new Node(MySQLConstants.DBEntity.PRIMARY_KEY);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.COLUMN_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            primary.setAttrs(attrs);
            primaryList.add(primary);
        }
        PKs.addChildren(primaryList);
        return table;
    }

    private Node findPrimaries(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.PRIMARY_KEYS);
        if (nodeForLoading == null) {
            Node PKs = new Node(MySQLConstants.NodeNames.PRIMARY_KEYS);
            PKs.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.PRIMARY_KEYS);
            node.addChild(PKs);
            nodeForLoading = PKs;
        }
        return nodeForLoading;
    }
}
