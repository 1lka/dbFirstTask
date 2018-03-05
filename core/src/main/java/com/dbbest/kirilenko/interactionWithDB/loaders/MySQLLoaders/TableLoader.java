package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@EntityLoader(element = MySQLConstants.DBEntity.TABLE)
public class TableLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "where TABLE_SCHEMA = ? and TABLE_TYPE = 'BASE TABLE' order by TABLE_NAME";

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
        return null;
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
            String tableName = node.getAttrs().get(MySQLConstants.AttributeName.TABLE_NAME);

            //todo загружаем таблицу
        } else if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            //todo загружаем все таблицы для данной схемы
        }
        return null;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> tables = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.SCHEMA_NAME);
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node table = new Node(MySQLConstants.DBEntity.TABLE);
            Map<String, String> attrs = fillAttributes(resultSet);
            table.setAttrs(attrs);
            tables.add(table);
        }
        return tables;
    }
}
