package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

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

@EntityLoader(element = MySQLConstants.DBEntity.FUNCTION)
public class FunctionLoader extends Loader{

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'FUNCTION' order by SPECIFIC_NAME";

    public FunctionLoader() {
    }

    public FunctionLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node loadElement(Node node) throws SQLException {
        return node;
    }

    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        if (MySQLConstants.DBEntity.FUNCTION.equals(node.getName())) {
            Loader paramsLoader = new RoutineParamsLoader(getConnection());
            List<Node> paramList = paramsLoader.loadCategory(node);
            Node parameters = new Node(MySQLConstants.NodeNames.PARAMETERS);
            parameters.addChildren(paramList);
            node.addChild(parameters);
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
        }
        return node;
    }

    @Override
    public List<Node> loadCategory(Node node) throws SQLException {
        List<Node> functions = new ChildrenList<>();
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.SCHEMA_NAME);
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node function = new Node(MySQLConstants.DBEntity.FUNCTION);
            Map<String, String> attrs = fillAttributes(resultSet);
            function.setAttrs(attrs);
            functions.add(function);
        }
        return functions;
    }
}
