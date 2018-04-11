package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EntityLoader(element = MySQLConstants.DBEntity.SCHEMA)
public class SchemaLoader extends Loader {

    private static final String SQL_QUERY =
            "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

    public SchemaLoader() {
    }

    public SchemaLoader(Connection connection) {
        super(connection);
    }

    /**
     * loads all attributes for schema node
     *
     * @param node for attribute loading
     * @return loaded schema node
     */
    @Override
    public Node loadElement(Node node) throws SQLException {
        String schema = node.getAttrs().get(MySQLConstants.AttributeName.NAME);
        ResultSet resultSet = executeQuery(SQL_QUERY, schema);
        if (resultSet.next()) {
            Map<String, String> attrs = fillAttributes(resultSet);
            String name = attrs.remove(MySQLConstants.AttributeName.SCHEMA_NAME);
            attrs.put(MySQLConstants.AttributeName.NAME, name);
            node.setAttrs(attrs);
            return node;
        } else {
            throw new LoadingException("there is no such schema: " + schema);
        }
    }

    /**
     * loads tables, views and routines attributes for schema
     *
     * @param node for children loading
     * @return node with lazy loaded children
     */
    @Override
    public Node lazyChildrenLoad(Node node) throws SQLException {
        Loader tableLoader = new TableLoader(getConnection());
        Loader viewLoader = new ViewLoader(getConnection());
        Loader procedureLoader = new ProcedureLoader(getConnection());
        Loader functionLoader = new FunctionLoader(getConnection());

        tableLoader.loadCategory(node);
        viewLoader.loadCategory(node);
        procedureLoader.loadCategory(node);
        functionLoader.loadCategory(node);

        return node;
    }

    /**
     * loads full tree of schema node
     *
     * @param node for full loading
     * @return fully loaded tree
     */
    @Override
    public Node fullLoadElement(Node node) throws SQLException {
        this.loadElement(node);
        Loader tableLoader = new TableLoader(getConnection());
        Loader viewLoader = new ViewLoader(getConnection());
        Loader procedureLoader = new ProcedureLoader(getConnection());
        Loader functionLoader = new FunctionLoader(getConnection());

        tableLoader.fullLoadElement(node);
        viewLoader.fullLoadElement(node);
        procedureLoader.fullLoadElement(node);
        functionLoader.fullLoadElement(node);

        return node;
    }

    @Override
    public Node loadCategory(Node node) {
        return null;
    }

    @Override
    public Node fullLoadCategory(Node node)  {
        return null;
    }

}