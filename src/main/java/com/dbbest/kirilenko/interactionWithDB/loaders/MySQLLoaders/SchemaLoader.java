package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.*;
import java.util.Map;

@Load(element = DBElement.SCHEMA)
public class SchemaLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

    public SchemaLoader() {
    }

    public SchemaLoader(Connection connection) {
        super(connection);
    }

    @Override
    public Node lazyLoad(String schema) throws SQLException {
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        if (resultSet.next()) {
            Node schemaNode = new Node(DBElement.SCHEMA);
            Map<String, String> attrs = fillAttributes(resultSet);
            schemaNode.setAttrs(attrs);
            loadChildren(schemaNode, schema);
            return schemaNode;
        } else {
            throw new RuntimeException("no such schema");
        }
    }

    private void loadChildren(Node schemaNode, String schema) throws SQLException {
        Loader tableLoader = new TableLoader(getConnection());
        Node tables = tableLoader.lazyLoad(schema);
        schemaNode.addChild(tables);

        Loader viewLoader = new ViewLoader(getConnection());
        Node views = viewLoader.lazyLoad(schema);
        schemaNode.addChild(views);

        Loader procedureLoader = new ProcedureLoader(getConnection());
        Node procedures = procedureLoader.lazyLoad(schema);
        schemaNode.addChild(procedures);

        Loader functionLoader = new FunctionLoader(getConnection());
        Node functions = functionLoader.lazyLoad(schema);
        schemaNode.addChild(functions);
    }

    @Override
    public void loadElement(Node node) {
    }


}
