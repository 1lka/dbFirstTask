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

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? " +
                    "order by ROUTINE_TYPE,SPECIFIC_NAME,ORDINAL_POSITION";

    private static final String LOAD_CATEGORY_QUERY =
            "select PARAMETER_NAME from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? order by ORDINAL_POSITION";

    private static final String ELEMENT_QUERY =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? and PARAMETER_NAME = ?";

    private static final String ELEMENT_QUERY_WITH_NULL =
            "select * from INFORMATION_SCHEMA.PARAMETERS " +
                    "where SPECIFIC_SCHEMA = ? and SPECIFIC_NAME = ? and PARAMETER_NAME is NULL";

    public RoutineParamsLoader() {
    }

    public RoutineParamsLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (MySQLConstants.NodeNames.PARAMETERS.equals(node.getName())) {
            return node;
        }
        String parameterName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String specificName = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schemaName = node.getParent().getParent().getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet;
        if (parameterName == null) {
            resultSet = executeQuery(ELEMENT_QUERY_WITH_NULL,schemaName, specificName);
        } else {
            resultSet = executeQuery(ELEMENT_QUERY, schemaName, specificName , parameterName);
        }
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.PARAMETER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        }
        throw new LoadingException("cant load parameter " + parameterName + " in " + schemaName + " schema");
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        String nodeName = node.getName();
        if (MySQLConstants.DBEntity.PARAMETER.equals(nodeName)) {
            loadElement(node);
        } else {
            Node params = findParams(node);
            fullLoadCategory(params.getParent());
        }
        return node;
    }

    @Override
    public Node loadCategory(Node routine) throws SQLException {
        return loadAll(LOAD_CATEGORY_QUERY, routine);
    }

    @Override
    public Node fullLoadCategory(Node routine) throws SQLException {
        return loadAll(FULL_LOAD_CATEGORY_QUERY, routine);
    }

    private Node loadAll(String query, Node routine) throws SQLException {
        String schemaName = routine.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        String routineName = routine.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName, routineName);

        Node params = findParams(routine);
        if (params == null) {
            params = new Node(MySQLConstants.NodeNames.PARAMETERS);
            routine.addChild(params);
        }
        params.getChildren().clear();
        List<Node> paramList = new ChildrenList<>();

        while (resultSet.next()) {
            Node param = new Node(MySQLConstants.DBEntity.PARAMETER);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.PARAMETER_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            param.setAttrs(attrs);
            paramList.add(param);
        }
        params.addChildren(paramList);
        return routine;
    }

    private Node findParams(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.PARAMETERS);
        if (nodeForLoading == null) {
            Node params = new Node(MySQLConstants.NodeNames.PARAMETERS);
            node.addChild(params);
            nodeForLoading = params;
        }
        return nodeForLoading;
    }
}
