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

public class TableIndexLoaderTest {

    private static TableIndexLoader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new TableIndexLoader(connection);
    }

    @Before
    public void start() {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");
        Node tables = new Node(MySQLConstants.NodeNames.TABLES);
        table = new Node(MySQLConstants.DBEntity.TABLE);
        indexes = new Node(MySQLConstants.NodeNames.INDEXES);
        table.getAttrs().put("NAME", "address");
        schema.addChild(tables);
        tables.addChild(table);
        table.addChild(indexes);
    }

    private Node table;
    private Node indexes;

    @Test
    public void loadElement() throws SQLException {
        Node index = new Node(MySQLConstants.DBEntity.INDEX);
        indexes.addChild(index);
        index.getAttrs().put("NAME", "idx_fk_city_id");
        System.out.println(loader.loadElement(index));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node index = new Node(MySQLConstants.DBEntity.INDEX);
        indexes.addChild(index);
        index.getAttrs().put("NAME", "idx_fk_city_id");
        System.out.println(loader.fullLoadElement(index));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(table));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(indexes));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(table));
    }

    @Test
    public void fullLoadCategory() throws SQLException {
        System.out.println(loader.fullLoadCategory(table));
    }

}