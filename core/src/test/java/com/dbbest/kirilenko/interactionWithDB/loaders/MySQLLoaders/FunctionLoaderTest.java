package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Connection4Test;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class FunctionLoaderTest {
    private static Loader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new FunctionLoader(connection);
    }

    private Node func;
    private Node functions;
    private Node schema;

    @Before
    public void before() {
        schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");

        functions = new Node(MySQLConstants.NodeNames.FUNCTIONS);
        schema.addChild(functions);

        func = new Node(MySQLConstants.DBEntity.FUNCTION);
        functions.addChild(func);
        func.getAttrs().put("NAME", "get_customer_balance");
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(loader.loadElement(func));
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(loader.lazyChildrenLoad(func));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        System.out.println(loader.fullLoadElement(func));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(functions));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(schema));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(functions));
    }

    @Test
    public void loadCategory2() throws SQLException {
        System.out.println(loader.loadCategory(schema));
    }
}