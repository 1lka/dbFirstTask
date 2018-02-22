package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.sql.*;
import java.util.Map;

public class RoutineParamsLoader extends AdditionalLoader {

//    private static final String SQL_QUERY = "select * from INFORMATION_SCHEMA.PARAMETERS where SPECIFIC_SCHEMA = ? order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";


    private static final String LOAD_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ?";

    private static final String ROUTINE_SCHEMA = "ROUTINE_SCHEMA";

    private static final String ROUTINE_NAME = "ROUTINE_NAME";

    public RoutineParamsLoader() {
    }

    public RoutineParamsLoader(Connection connection) {
        super(connection);
    }

    @Override
    public void loadDetails(Node node) throws SQLException {
        Node parameters = new Node(DBElement.PARAMETERS);
        node.addChild(parameters);

        String schemaName = node.getAttrs().get(ROUTINE_SCHEMA);
        String routineName = node.getAttrs().get(ROUTINE_NAME);
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, routineName);

        while (resultSet.next()) {
            Node routine = new Node(DBElement.PARAMETER);
            Map<String, String> attrs = fillAttributes(resultSet);
            routine.setAttrs(attrs);
            parameters.addChild(routine);
        }
    }
}
