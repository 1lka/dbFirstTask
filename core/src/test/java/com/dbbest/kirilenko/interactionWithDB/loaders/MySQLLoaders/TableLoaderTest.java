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

    private static Node root;
    private static Loader tableLoader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        tableLoader = new TableLoader(connection);
    }

    @Before
    public void before() {
        root = new Node(MySQLConstants.DBEntity.TABLE);
        String schemaName = "sakila";
        String tableName = "film";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.TABLE_SCHEMA, schemaName);
        attrs.put(MySQLConstants.AttributeName.NAME, tableName);
        root.setAttrs(attrs);
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        tableLoader.lazyChildrenLoad(root);
        System.out.println(root);
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println("loadElement");
        System.out.println(tableLoader.loadElement(root));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.NAME, schemaName);
        schema.setAttrs(attrs);
        System.out.println("fullLoad");
        System.out.println(tableLoader.fullLoadElement(schema));
    }

    @Test
    public void loadCategory() throws SQLException {
        Node node = new Node(MySQLConstants.DBEntity.SCHEMA);
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put("NAME", "sakila");
        node.setAttrs(attrs);
        System.out.println(tableLoader.loadCategory(node));
    }
}