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

public class FunctionLoaderTest {
    private static Node root;
    private static Loader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new FunctionLoader(connection);
    }

    @Before
    public void before() {
        root = new Node(MySQLConstants.DBEntity.FUNCTION);
        String schemaName = "sakila";
        String functionName = "get_customer_balance";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.ROUTINE_SCHEMA, schemaName);
        attrs.put(MySQLConstants.AttributeName.NAME, functionName);
        root.setAttrs(attrs);
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(loader.lazyChildrenLoad(root));
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(loader.loadElement(root));
    }

    @Test
    public void fullLoadElement() {

    }

    @Test
    public void loadCategory() throws SQLException {
        Node node = new Node(MySQLConstants.DBEntity.SCHEMA);
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put("NAME", "sakila");
        node.setAttrs(attrs);
        System.out.println(loader.loadCategory(node));
    }
}