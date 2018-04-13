package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Connection4Test;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class RoutineParamsLoaderTest {

    private static RoutineParamsLoader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new RoutineParamsLoader(connection);
    }

    @Before
    public void start() {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");
        Node functions = new Node(MySQLConstants.NodeNames.FUNCTIONS);
        function = new Node(MySQLConstants.DBEntity.FUNCTION);
        params = new Node(MySQLConstants.NodeNames.PARAMETERS);
        function.getAttrs().put("NAME", "get_customer_balance");
        schema.addChild(functions);
        functions.addChild(function);
        function.addChild(params);
    }

    private Node function;
    private Node params;

    @Test
    public void loadElement() throws SQLException {
        Node param = new Node(MySQLConstants.DBEntity.PARAMETER);
        params.addChild(param);
        param.getAttrs().put("NAME", null);
        System.out.println(loader.loadElement(param));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node param = new Node(MySQLConstants.DBEntity.PARAMETER);
        params.addChild(param);
        param.getAttrs().put("NAME", null);
        System.out.println(loader.fullLoadElement(param));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(function));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(params));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(function));
    }

    @Test
    public void fullLoadCategory() throws SQLException {
        System.out.println(loader.fullLoadCategory(function));
    }
}