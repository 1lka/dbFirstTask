package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Connection4Test;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
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
        String tableName = "actor";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.TABLE_SCHEMA, schemaName);
        attrs.put(MySQLConstants.AttributeName.TABLE_NAME, tableName);
        root.setAttrs(attrs);
    }

    @Test
    public void lazyChildrenLoad() {
    }

    @Test
    public void loadElement() {
    }

    @Test
    public void fullLoadElement() {
    }

    @Test
    public void loadCategory() {
    }
}