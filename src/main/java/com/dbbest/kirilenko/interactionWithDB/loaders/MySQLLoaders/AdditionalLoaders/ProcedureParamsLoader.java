package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class ProcedureParamsLoader implements AdditionalLoader {

    private static final String SQL_QUERY = "select * from INFORMATION_SCHEMA.PARAMETERS where SPECIFIC_SCHEMA = ? order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";

    private static final String SPECIFIC_NAME = "SPECIFIC_NAME";

    @Override
    public void load(Node routines, Connection connection) throws SQLException {
        String schema = connection.getCatalog();
        PreparedStatement ps = connection.prepareStatement(SQL_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, schema);
        ResultSet rs = ps.executeQuery();

        Node functions = routines.wideSearch(DBElement.FUNCTIONS);
        for (Node routine : functions.getChildren()) {
            fillRoutines(routine, rs);
        }

        Node procedures = routines.wideSearch(DBElement.PROCEDURES);
        for (Node procedure : procedures.getChildren()) {
            fillRoutines(procedure,rs);
        }
    }

    private void fillRoutines(Node routine, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        Node parameters = new Node(DBElement.PARAMETERS);
        routine.addChild(parameters);

        String name = routine.getAttrs().get(SPECIFIC_NAME);
        while (rs.next()) {
            String routineName = rs.getString(SPECIFIC_NAME);
            if (routineName.equals(name)) {
                Node parameter = new Node(DBElement.PARAMETER);
                Map<String, String> attrs = parameter.getAttrs();
                for (int i = 1; i <= columnsCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = String.valueOf(rs.getObject(i));
                    attrs.put(columnName, columnValue);
                }
                parameters.addChild(parameter);
            } else {
                rs.previous();
                return;
            }
        }
    }
}
