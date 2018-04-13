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

public class TableColumnLoaderTest {

    private static TableColumnLoader loader;

    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new TableColumnLoader(connection);
    }

    @Before
    public void start() {
        Node schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");
        Node tables = new Node(MySQLConstants.NodeNames.TABLES);
        table = new Node(MySQLConstants.DBEntity.TABLE);
        columns = new Node(MySQLConstants.NodeNames.COLUMNS);
        table.getAttrs().put("NAME", "actor");
        schema.addChild(tables);
        tables.addChild(table);
        table.addChild(columns);
    }

    private Node table;
    private Node columns;

    @Test
    public void loadElement() throws SQLException {
        Node column = new Node(MySQLConstants.DBEntity.COLUMN);
        columns.addChild(column);
        column.getAttrs().put("NAME", "actor_id");
        System.out.println(loader.loadElement(column));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        Node column = new Node(MySQLConstants.DBEntity.COLUMN);
        columns.addChild(column);
        column.getAttrs().put("NAME", "actor_id");
        System.out.println(loader.fullLoadElement(column));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(table));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(columns));
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