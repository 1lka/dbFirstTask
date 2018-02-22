package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.RoutineParamsLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Load(element = DBElement.FUNCTION)
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
        Node functions = new Node(DBElement.FUNCTIONS);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node function = new Node(DBElement.FUNCTION);
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
