package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
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

    private static final String SQL_QUERY_ALL_PARAMS_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? " +
                    "order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";

    private static final String SQL_LAZY_QUERY =
            "select PARAMETER_NAME,SPECIFIC_NAME from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? order by ORDINAL_POSITION";

    private static final String SQL_FULL_ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_NAME = ? and PARAMETER_NAME = ? order by ORDINAL_POSITION";

    private static final String SQL_FULL_ELEMENT_QUERY_WITH_NULL =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_NAME = ? and PARAMETER_NAME is NULL order by ORDINAL_POSITION";


    public RoutineParamsLoader() {
    }

    public RoutineParamsLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        String routineName = node.getAttrs().get(MySQLConstants.AttributeName.SPECIFIC_NAME);
        String param = node.getAttrs().get(MySQLConstants.AttributeName.NAME);

        ResultSet resultSet;
        if (param == null) {
            resultSet = executeQuery(SQL_FULL_ELEMENT_QUERY_WITH_NULL, routineName);
        } else {
            resultSet = executeQuery(SQL_FULL_ELEMENT_QUERY, routineName, param);
        }
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.PARAMETER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such parameter: " + routineName);
        }
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (node.equals(MySQLConstants.DBEntity.PARAMETER)) {
            return loadElement(node);
        } else if (nodeName.equals(MySQLConstants.DBEntity.FUNCTION) || nodeName.equals(MySQLConstants.DBEntity.PROCEDURE)) {
            Node parameters = node.wideSearch(MySQLConstants.NodeNames.PARAMETERS);
            List<Node> paramsList;
            if (parameters == null) {
                parameters = new Node(MySQLConstants.NodeNames.PARAMETERS);
                node.addChild(parameters);
            }
            paramsList = loadAllParams(node);
            parameters.setChildren(paramsList);
        }
        return node;
    }

    private List<Node> loadAllParams(Node node) throws SQLException {
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.ROUTINE_SCHEMA);
        String routinrName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        List<Node> list = new ChildrenList<>();
        ResultSet resultSet = executeQuery(SQL_QUERY_ALL_PARAMS_QUERY, schema, routinrName);
        while (resultSet.next()) {
            Node parameter = new Node(MySQLConstants.DBEntity.PARAMETER);
            Map<String, String> attrs = fillAttributes(resultSet);

            String name = attrs.remove(MySQLConstants.AttributeName.PARAMETER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);

            parameter.setAttrs(attrs);
            list.add(parameter);
        }
        return list;
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
