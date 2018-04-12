package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.exceptions.LoadingException;
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

import static org.junit.Assert.*;

public class SchemaLoaderTest {

    private Node root;
    private static Loader schemaLoader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        schemaLoader = new SchemaLoader(connection);
    }

    @Before
    public void before() {
        root = new Node(MySQLConstants.DBEntity.SCHEMA);
        root.getAttrs().put("NAME", "sakila");
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(schemaLoader.lazyChildrenLoad(root));
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(schemaLoader.loadElement(root));
    }

    @Test
    public void fullLoad() throws SQLException {
        System.out.println(schemaLoader.fullLoadElement(root));
    }
}