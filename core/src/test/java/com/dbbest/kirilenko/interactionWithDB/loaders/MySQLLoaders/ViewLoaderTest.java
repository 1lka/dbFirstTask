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

public class ViewLoaderTest {
    private static Node root;
    private static Loader viewLoader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        viewLoader = new ViewLoader(connection);
    }

    @Before
    public void before() {
        root = new Node(MySQLConstants.DBEntity.VIEW);
        String schemaName = "sakila";
        String tableName = "actor_info";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.NAME, tableName);
        attrs.put(MySQLConstants.AttributeName.TABLE_SCHEMA, schemaName);
        root.setAttrs(attrs);
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(viewLoader.loadElement(root));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        System.out.println(viewLoader.loadElement(root));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        Node node = new Node(MySQLConstants.DBEntity.SCHEMA);
        Map<String, String> attrs = new HashMap<>();
        attrs.put("NAME", "sakila");
        node.setAttrs(attrs);
        System.out.println(viewLoader.fullLoadElement(node));
    }

    @Test
    public void loadCategory() throws SQLException {
        Node node = new Node(MySQLConstants.DBEntity.SCHEMA);
        Map<String, String> attrs = new HashMap<>();
        attrs.put("NAME", "sakila");
        node.setAttrs(attrs);
        System.out.println(viewLoader.loadCategory(node));
    }
}