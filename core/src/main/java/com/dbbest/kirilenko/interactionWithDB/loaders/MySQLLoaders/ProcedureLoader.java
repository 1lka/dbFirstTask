package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.AdditionalLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders.RoutineParamsLoader;

import java.sql.*;

@Load(element = DBElement.PROCEDURE)
public class ProcedureLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = ? and ROUTINE_TYPE = 'PROCEDURE' order by SPECIFIC_NAME";

    public ProcedureLoader() {
    }

    public ProcedureLoader(Connection connection) {
        super(connection);
    }


    @Override
    public Node lazyLoad(String schema) throws SQLException {
        Node procedures = new Node(DBElement.PROCEDURES);

        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        while (resultSet.next()) {
            Node procedure = new Node(DBElement.PROCEDURE);
            procedure.setAttrs(fillAttributes(resultSet));
            procedures.addChild(procedure);
        }
        return procedures;
    }

    @Override
    public void loadElement(Node node) throws SQLException {
        AdditionalLoader loader = new RoutineParamsLoader(getConnection());
        loader.loadDetails(node);
    }


}
