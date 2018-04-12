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
import java.util.HashMap;
import java.util.Map;

public class TableLoaderTest {

    private static Loader loader;
    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new TableLoader(connection);
    }

    private Node table;
    private Node tables;
    private Node schema;

    @Before
    public void before() {
        schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");

        tables = new Node(MySQLConstants.NodeNames.TABLES);
        schema.addChild(tables);

        table = new Node(MySQLConstants.DBEntity.TABLE);
        tables.addChild(table);
        table.getAttrs().put("NAME", "film");
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(loader.loadElement(table));
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(loader.lazyChildrenLoad(table));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        System.out.println(loader.fullLoadElement(schema));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(tables));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(table));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(tables));
    }

    @Test
    public void loadCategory2() throws SQLException {
        System.out.println(loader.loadCategory(schema));
    }
}