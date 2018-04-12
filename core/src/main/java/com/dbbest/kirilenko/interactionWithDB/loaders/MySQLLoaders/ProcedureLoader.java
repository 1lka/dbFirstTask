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

@EntityLoader(element = {MySQLConstants.DBEntity.PROCEDURE, MySQLConstants.NodeNames.PROCEDURES})
public class ProcedureLoader extends Loader {

    private static final String ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_NAME = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";

    private static final String LOAD_CATEGORY_QUERY =
            "SELECT ROUTINE_NAME FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";

    private static final String FULL_LOAD_CATEGORY_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES " +
                    "where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";


    public ProcedureLoader() {
    }

    public ProcedureLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        String procedureName = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        String schema = node.getParent().getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(ELEMENT_QUERY, schema, procedureName);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such procedure: " + procedureName);
        }
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        if (!MySQLConstants.DBEntity.PROCEDURE.equals(node.getName())) {
            return node;
        }
        Loader paramsLoader = new RoutineParamsLoader(getConnection());
        paramsLoader.loadCategory(node);
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.PROCEDURE.equals(node.getName())) {
            loadElement(node);
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
            paramsLoader.fullLoadElement(node);
            return node;
        } else {
            Node procedures = findProcedures(node);
            fullLoadCategory(procedures);
            for (Node procedure : procedures.getChildren()) {
                fullLoadElement(procedure);
            }
        }
        return node;
    }

    @Override
    public Node loadCategory(Node node) throws SQLException {
        Node nodeForLoading = findProcedures(node);
        List<Node> tables = loadAll(LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(tables);
        return node;
    }

    @Override
    public Node fullLoadCategory(Node node) throws SQLException {
        Node nodeForLoading = findProcedures(node);
        List<Node> procedures = loadAll(FULL_LOAD_CATEGORY_QUERY, nodeForLoading);
        nodeForLoading.addChildren(procedures);
        return node;
    }

    private List<Node> loadAll(String query, Node procedureNode) throws SQLException {
        List<Node> procedures = new ChildrenList<>();
        String schemaName = procedureNode.getParent().getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(query, schemaName);
        while (resultSet.next()) {
            Node func = new Node(MySQLConstants.DBEntity.PROCEDURE);
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            func.setAttrs(attrs);
            procedures.add(func);
        }
        return procedures;
    }

    private Node findProcedures(Node node) {
        Node nodeForLoading = node.wideSearch(MySQLConstants.NodeNames.PROCEDURES);
        if (nodeForLoading == null) {
            Node tables = new Node(MySQLConstants.NodeNames.PROCEDURES);
            node.addChild(tables);
            nodeForLoading = tables;
        }
        nodeForLoading.getChildren().clear();
        return nodeForLoading;
    }
}
