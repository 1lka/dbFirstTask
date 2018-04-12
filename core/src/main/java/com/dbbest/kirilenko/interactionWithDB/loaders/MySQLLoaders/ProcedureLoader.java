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

    private static final String SQL_LAZY_QUERY =
            "SELECT ROUTINE_NAME,ROUTINE_SCHEMA  FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";

    private static final String SQL_FULL_ELEMENT_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_NAME = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";


    public ProcedureLoader() {
    }

    public ProcedureLoader(Connection connection) {
        super(connection);
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
            throw new LoadingException("there is no such procedure: " + procedureName);
        }
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        Loader paramsLoader = new RoutineParamsLoader(getConnection());
        paramsLoader.loadCategory(node);
        Node columns = new Node(MySQLConstants.NodeNames.PARAMETERS);

        node.addChild(columns);
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.PROCEDURE.equals(node.getName())) {
            this.loadElement(node);
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
             paramsLoader.loadCategory(node);
            Node parameters = new Node(MySQLConstants.NodeNames.PARAMETERS);

            node.addChild(parameters);
            return node;
        } else if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            Node procedures = node.wideSearch(MySQLConstants.NodeNames.PROCEDURES);
            List<Node> procedureList;
            if (procedures == null) {
                procedures = new Node(MySQLConstants.NodeNames.PROCEDURES);
                node.addChild(procedures);
//                procedureList = loadCategory(node);
//                procedures.addChildren(procedureList);
            } else {
                procedureList = procedures.getChildren();
            }
//            for (Node procedure : procedureList) {
//                fullLoadElement(procedure);
//            }
//            procedures.getAttrs().put("childrenCount", String.valueOf(procedureList.size()));
        }
        return node;
    }

    @Override
    public Node loadCategory(Node node) throws SQLException {
        List<Node> procedures = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(SQL_LAZY_QUERY, schema);
        while (resultSet.next()) {
            Node procedure = new Node(MySQLConstants.DBEntity.PROCEDURE);
            Map<String, String> attrs = fillAttributes(resultSet);

            String name = attrs.remove(MySQLConstants.AttributeName.ROUTINE_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);

            procedure.setAttrs(attrs);
            procedures.add(procedure);
        }
        return node;
    }

    @Override
    public Node fullLoadCategory(Node node) throws SQLException {
        return null;
    }
}
