package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.RoutineParamsLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public Node lazyLoad(String schema) throws SQLException {
        Node functions = new Node(MySQLConstants.NodeNames.FUNCTIONS);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node function = new Node(MySQLConstants.DBEntity.FUNCTION);
            function.setAttrs(fillAttributes(resultSet));
            functions.addChild(function);
        }
        return functions;
    }

    @Override
    public void loadElement(Node node) throws SQLException {
        AdditionalLoader loader = new RoutineParamsLoader(getConnection());
        loader.loadDetails(node);
    }
}
