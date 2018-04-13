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

public class TableForeignKeyLoaderTest {

    private static TableForeignKeyLoader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new TableForeignKeyLoader(connection);
    }

    @Before
    public void start() {
        schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");
        tables = new Node(MySQLConstants.NodeNames.TABLES);
        table = new Node(MySQLConstants.DBEntity.TABLE);
        FKs = new Node(MySQLConstants.NodeNames.FOREIGN_KEYS);
        table.getAttrs().put("NAME", "city");
        schema.addChild(tables);
        tables.addChild(table);
        table.addChild(FKs);
    }

    private Node schema;
    private Node tables;
    private Node table;
    private Node FKs;

    @Test
    public void loadElement() throws SQLException {
        Node FK = new Node(MySQLConstants.DBEntity.FOREIGN_KEY);
        FKs.addChild(FK);
        FK.getAttrs().put("NAME", "fk_city_country");
        System.out.println(loader.loadElement(FK));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node FK = new Node(MySQLConstants.DBEntity.FOREIGN_KEY);
        FKs.addChild(FK);
        FK.getAttrs().put("NAME", "fk_city_country");
        System.out.println(loader.fullLoadElement(FK));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(table));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(FKs));
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