package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.PARAMETER, MySQLConstants.NodeNames.PARAMETERS})
public class RoutineParamsLoader extends Loader {

//    private static final String SQL_QUERY = "select * from INFORMATION_SCHEMA.PARAMETERS where SPECIFIC_SCHEMA = ? order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";

    private static final String SQL_LAZY_QUERY =
            "select PARAMETER_NAME from INFORMATION_SCHEMA.PARAMETERS " +
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
        String schemaName = node.getAttrs().get(MySQLConstants.AttributeName.ROUTINE_SCHEMA);
        String routineName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);

        List<Node> paramList = new ChildrenList<>();
        ResultSet resultSet = executeQuery(SQL_LAZY_QUERY, schemaName, routineName);
        while (resultSet.next()) {
            Node parameter = new Node(MySQLConstants.DBEntity.PARAMETER);
            Map<String, String> attrs = fillAttributes(resultSet);

            String name = attrs.remove(MySQLConstants.AttributeName.PARAMETER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);

            parameter.setAttrs(attrs);
            paramList.add(parameter);
        }
        return paramList;
    }
}
