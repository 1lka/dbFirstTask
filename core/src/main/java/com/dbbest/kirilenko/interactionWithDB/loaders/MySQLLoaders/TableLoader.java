package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public Node lazyLoad(String schema) throws SQLException {
        Node tables = new Node(MySQLConstants.NodeNames.TABLES);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node table = new Node(MySQLConstants.DBEntity.TABLE);
            table.setAttrs(fillAttributes(resultSet));
            tables.addChild(table);
        }
        return tables;
    }

    @Override
    public void loadElement(Node node) throws SQLException {
        AdditionalLoader loader = new TableColumnLoader(getConnection());
        loader.loadDetails(node);

        loader = new TableIndexLoader(getConnection());
        loader.loadDetails(node);

        loader = new TableForeignKeyLoader(getConnection());
        loader.loadDetails(node);

        loader = new TablePrimaryKeyLoader(getConnection());
        loader.loadDetails(node);

        loader = new TableTriggerLoader(getConnection());
        loader.loadDetails(node);
    }
}
