package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class RoutineParamsLoader extends Loader {

//    private static final String SQL_QUERY = "select * from INFORMATION_SCHEMA.PARAMETERS where SPECIFIC_SCHEMA = ? order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";

    private static final String LOAD_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? order by ORDINAL_POSITION";

    public RoutineParamsLoader() {
    }

    public RoutineParamsLoader(Connection connection) {
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
    public List<Node> loadCategory(Node node) throws SQLException {
        String schemaName = node.getAttrs().get("ROUTINE_SCHEMA");
        String routineName = node.getAttrs().get("ROUTINE_NAME");

        List<Node> paramList = new ChildrenList<>();
        ResultSet resultSet = executeQuery(LOAD_ELEMENT_QUERY, schemaName, routineName);
        while (resultSet.next()) {
            Node parameter = new Node(MySQLConstants.DBEntity.PARAMETER);
            Map<String, String> attrs = fillAttributes(resultSet);
            parameter.setAttrs(attrs);
            paramList.add(parameter);
        }
        return paramList;
    }

}
