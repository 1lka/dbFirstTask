package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class ColumnLoader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
                    "where TABLE_SCHEMA = ? and TABLE_NAME not in " +
                    "(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = ?) order by TABLE_NAME";

    private static final String TABLE_NAME = "TABLE_NAME";

    public void loadColumns(Node tables, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, schema);
        ps.setString(2, schema);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();


        //todo create mechanism for filling columns in tablesst
        while (rs.next()) {
            String tableName = rs.getString(TABLE_NAME);


        }
    }




}
