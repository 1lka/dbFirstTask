package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.RoutineParamsLoader;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@EntityLoader(element = {MySQLConstants.DBEntity.FUNCTION, MySQLConstants.NodeNames.FUNCTIONS})
public class FunctionLoader extends Loader {

    private static final String SQL_LAZY_QUERY =
            "SELECT ROUTINE_NAME ,ROUTINE_SCHEMA FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'FUNCTION' order by SPECIFIC_NAME";

    private static final String SQL_FULL_ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_NAME = ? and ROUTINE_TYPE = 'FUNCTION' order by SPECIFIC_NAME";

    public FunctionLoader() {
    }

    public FunctionLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        Loader paramsLoader = new RoutineParamsLoader(getConnection());
        List<Node> paramsList = paramsLoader.loadCategory(node);
        Node columns = new Node(MySQLConstants.NodeNames.PARAMETERS);
        columns.addChildren(paramsList);
        node.addChild(columns);
        return node;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        String procedureName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.ROUTINE_SCHEMA);
        ResultSet resultSet = executeQuery(SQL_FULL_ELEMENT_QUERY, schema, procedureName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such function: " + procedureName);
        }
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.FUNCTION.equals(node.getName())) {
            loadElement(node);
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
            paramsLoader.fullLoadElement(node);
            return node;
        } else if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            Node functions = node.wideSearch(MySQLConstants.NodeNames.FUNCTIONS);
            List<Node> functionList;
            if (functions == null) {
                functions = new Node(MySQLConstants.NodeNames.FUNCTIONS);
                node.addChild(functions);
                functionList = loadCategory(node);
                functions.addChildren(functionList);
            } else {
                functionList = functions.getChildren();
            }
            for (Node procedure : functionList) {
                fullLoadElement(procedure);
            }
            functions.getAttrs().put("childrenCount", String.valueOf(functionList.size()));
        }

        return node;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> functions = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(SQL_LAZY_QUERY, schema);
        while (resultSet.next()) {
            Node function = new Node(MySQLConstants.DBEntity.FUNCTION);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            function.setAttrs(attrs);
            functions.add(function);
        }
        return functions;
    }
}
