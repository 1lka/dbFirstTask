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
import java.util.HashMap;
import java.util.Map;

public class RoutineParamsLoaderTest {

    private static Node root;
    private static Loader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new RoutineParamsLoader(connection);
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
    public void loadElement() {
    }

    @Test
    public void fullLoadElement() {
    }

    @Test
    public void loadCategory() throws SQLException {
        Node node = new Node(MySQLConstants.DBEntity.FUNCTION);
        Map<String,String> attrs = new HashMap<String, String>();
        attrs.put(MySQLConstants.AttributeName.ROUTINE_SCHEMA, "sakila");
        attrs.put(MySQLConstants.AttributeName.NAME, "get_customer_balance");
        node.setAttrs(attrs);
        System.out.println(loader.loadCategory(node));
    }
}