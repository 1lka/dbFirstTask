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

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_NAME = ? and ROUTINE_TYPE = 'FUNCTION'";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT ROUTINE_NAME FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'FUNCTION' order by SPECIFIC_NAME";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'FUNCTION' order by SPECIFIC_NAME";

    public FunctionLoader() {
    }

    public FunctionLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        if (!MySQLConstants.DBEntity.FUNCTION.equals(node.getName())) {
            return node;
        }
        String functionName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schema = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schema, functionName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            markElementLoaded(node);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such function: " + functionName);
        }
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        if (!MySQLConstants.DBEntity.FUNCTION.equals(node.getName())) {
            return node;
        }
        Loader paramsLoader = new RoutineParamsLoader(getConnection());
        paramsLoader.loadCategory(node);
        markElementLazilyLoaded(node);
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.FUNCTION.equals(node.getName())) {
            if (!Boolean.valueOf(node.getAttrs().get(Loader.ELEMENT_LOADED))) {
                loadElement(node);
            }
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
            paramsLoader.fullLoadElement(node);
            markElementFullyLoaded(node);
            return node;
        } else {
            Node functions = findFunctions(node);
            fullLoadCategory(functions);
            RoutineParamsLoader routineParamsLoader = new RoutineParamsLoader();
            routineParamsLoader.loadFunctions(functions, getConnection());
            for (Node n : functions.getChildren()) {
                markElementFullyLoaded(n);
            }
        }
        return node;
    }

    @Override
    public Node loadCategory(Node node) throws SQLException {
        Node nodeForLoading = findFunctions(node);
        List<Node> tables = loadAll(LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    @Override
    public Node fullLoadCategory(Node node) throws SQLException {
        Node nodeForLoading = findFunctions(node);
        List<Node> functions = loadAll(FULL_LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(functions);
        return node;
    }

    private List<Node> loadAll(String query, Node functionsNode) throws SQLException {
        List<Node> functions = new ChildrenList<>();
        String schemaName = functionsNode.getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName);
        while (resultSet.next()) {
            Node func = new Node(MySQLConstants.DBEntity.FUNCTION);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            func.setAttrs(attrs);
            functions.add(func);
        }
        return functions;
    }

    private Node findFunctions(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.FUNCTIONS);
        if (nodeForLoading == null) {
            Node tables = new Node(MySQLConstants.NodeNames.FUNCTIONS);
            tables.getAttrs().put(MySQLConstants.AttributeName.NAME, MySQLConstants.NodeNames.FUNCTIONS);
            node.addChild(tables);
            nodeForLoading = tables;
        }
        nodeForLoading.getChildren().clear();
        return nodeForLoading;
    }
}
