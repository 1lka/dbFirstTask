package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.*;
import com.dbbest.kirilenko.tree.ChildrenList;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@EntityLoader(element = MySQLConstants.DBEntity.PROCEDURE)
public class ProcedureLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";

    public ProcedureLoader() {
    }

    public ProcedureLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return null;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.PROCEDURE.equals(node.getName())) {
            this.loadElement(node);
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
            List<Node> paramList = paramsLoader.loadCategory(node);
            Node parameters = new Node(MySQLConstants.NodeNames.PARAMETERS);
            parameters.addChildren(paramList);
            node.addChild(parameters);
            return node;
        } else if (MySQLConstants.DBEntity.SCHEMA.equals(node.getName())) {
            Node procedures = node.wideSearch(MySQLConstants.NodeNames.PROCEDURES);
            List<Node> procedureList;
            if (procedures == null) {
                procedures = new Node(MySQLConstants.NodeNames.PROCEDURES);
                node.addChild(procedures);
                procedureList = loadCategory(node);
                procedures.addChildren(procedureList);
            } else {
                procedureList = procedures.getChildren();
            }
            for (Node procedure : procedureList) {
                fullLoadElement(procedure);
            }
            procedures.getAttrs().put("childrenCount", String.valueOf(procedureList.size()));
        }
        return node;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> procedures = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.SCHEMA_NAME);
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node procedure = new Node(MySQLConstants.DBEntity.PROCEDURE);
            Map<String, String> attrs = fillAttributes(resultSet);
            procedure.setAttrs(attrs);
            procedures.add(procedure);
        }
        return procedures;
    }
}
