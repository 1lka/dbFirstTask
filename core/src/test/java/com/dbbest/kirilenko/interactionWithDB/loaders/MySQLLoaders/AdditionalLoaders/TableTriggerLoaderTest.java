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

public class TableTriggerLoaderTest {

    private static TableTriggerLoader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new TableTriggerLoader(connection);
    }

    @Before
    public void start() {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");
        Node tables = new Node(MySQLConstants.NodeNames.TABLES);
        table = new Node(MySQLConstants.DBEntity.TABLE);
        triggers = new Node(MySQLConstants.NodeNames.TRIGGERS);
        table.getAttrs().put("NAME", "film");
        schema.addChild(tables);
        tables.addChild(table);
        table.addChild(triggers);
    }

    private Node table;
    private Node triggers;

    @Test
    public void loadElement() throws SQLException {
        Node trigger = new Node(MySQLConstants.DBEntity.TRIGGER);
        triggers.addChild(trigger);
        trigger.getAttrs().put("NAME", "upd_film");
        System.out.println(loader.loadElement(trigger));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node column = new Node(MySQLConstants.DBEntity.TRIGGER);
        triggers.addChild(column);
        column.getAttrs().put("NAME", "upd_film");
        System.out.println(loader.fullLoadElement(column));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(table));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(triggers));
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