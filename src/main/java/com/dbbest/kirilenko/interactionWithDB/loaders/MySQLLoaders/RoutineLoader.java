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
    public Node lazyLoadWithChildren(String schema) throws SQLException {
        Node routines = new Node(DBElement.ROUTINES);
        Node procedures = new Node(DBElement.PROCEDURES);
        Node functions = new Node(DBElement.FUNCTIONS);
        routines.addChild(procedures);
        routines.addChild(functions);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            String routineType = resultSet.getString(ROUTINE_TYPE).toLowerCase();
            switch (routineType) {
                case DBElement.FUNCTION:
                    Node function = new Node(DBElement.FUNCTION);
                    function.setAttrs(fillAttributes(resultSet));
                    functions.addChild(function);
                    break;
                case DBElement.PROCEDURE:
                    Node procedure = new Node(DBElement.PROCEDURE);
                    procedure.setAttrs(fillAttributes(resultSet));
                    procedures.addChild(procedure);
                    break;
            }
        }
        return routines;
    }

}
