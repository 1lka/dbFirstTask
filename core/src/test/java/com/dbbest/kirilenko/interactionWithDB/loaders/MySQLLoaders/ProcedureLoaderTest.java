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

public class ProcedureLoaderTest {

    private static Loader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new ProcedureLoader(connection);
    }

    private Node proc;
    private Node procedures;
    private Node schema;

    @Before
    public void before() {
        schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");

        procedures = new Node(MySQLConstants.NodeNames.PROCEDURES);
        schema.addChild(procedures);

        proc = new Node(MySQLConstants.DBEntity.PROCEDURE);
        procedures.addChild(proc);
        proc.getAttrs().put("NAME", "film_in_stock");
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(loader.loadElement(proc));
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(loader.lazyChildrenLoad(proc));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        System.out.println(loader.fullLoadElement(proc));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(procedures));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(schema));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(procedures));
    }

    @Test
    public void loadCategory2() throws SQLException {
        System.out.println(loader.loadCategory(schema));
    }
}