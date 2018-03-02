package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Connection4Test;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SchemaLoaderTest {

    private static Connection connection;

    @BeforeClass
    public static void init() {
        connection = Connection4Test.getConnection();
    }

    @Test
    public void lazyChildrenLoad() {
    }

    @Test
    public void loadElement() throws SQLException {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.SCHEMA_NAME, schemaName);
        schema.setAttrs(attrs);
        Loader loader = new SchemaLoader(connection);
        System.out.println(loader.loadElement(schema));

    }

    @Test
    public void fullLoad() {
    }
}