package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.ProcedureParamsLoader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.ROUTINE, parent = SchemaLoader.class)
public class RoutineLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? order by SPECIFIC_NAME";

    private static final String ROUTINE_TYPE = "ROUTINE_TYPE";


    @Override
    public void lazyLoad(Node node, Connection connection) throws SQLException {
        Node routines = new Node(DBElement.ROUTINES);
        Node procedures = new Node(DBElement.PROCEDURES);
        Node functions = new Node(DBElement.FUNCTIONS);
        routines.addChild(procedures);
        routines.addChild(functions);
        node.addChild(routines);

        String schema = connection.getCatalog();
        PreparedStatement statement = connection.prepareStatement(SQL_QUERY);
        statement.setString(1, schema);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        Map<String, String> attrs;
        while (rs.next()) {
            String routineType = rs.getString(ROUTINE_TYPE).toLowerCase();
            switch (routineType) {
                case DBElement.FUNCTION:
                    Node function = new Node(DBElement.FUNCTION);
                    attrs = function.getAttrs();
                    for (int i = 1; i <= columnsCount; i++) {
                        String key = rsmd.getColumnName(i);
                        String value = String.valueOf(rs.getObject(i));
                        attrs.put(key, value);
                    }
                    functions.addChild(function);
                    break;
                case DBElement.PROCEDURE:
                    Node procedure = new Node(DBElement.PROCEDURE);
                    attrs = procedure.getAttrs();
                    for (int i = 1; i <= columnsCount; i++) {
                        String key = rsmd.getColumnName(i);
                        String value = String.valueOf(rs.getObject(i));
                        attrs.put(key, value);
                    }
                    procedures.addChild(procedure);
                    break;
            }
        }
    }

    @Override
    public void fullLoadOnLazy(Node node, Connection connection) throws SQLException {
        Node routines = node.wideSearch(DBElement.ROUTINES);

        AdditionalLoader proc = new ProcedureParamsLoader();
        proc.load(routines, connection);
    }

    @Override
    public Node fullLoad(Connection connection) throws SQLException {
        return null;
    }
}
