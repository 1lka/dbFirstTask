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

public class ViewLoaderTest {

    private static Loader loader;
    @BeforeClass
    public static void init() {
        Connection connection = Connection4Test.getConnection();
        loader = new ViewLoader(connection);
    }

    private Node view;
    private Node views;
    private Node schema;

    @Before
    public void before() {
        schema = new Node(MySQLConstants.DBEntity.SCHEMA);
        schema.getAttrs().put("NAME", "sakila");

        views = new Node(MySQLConstants.NodeNames.VIEWS);
        schema.addChild(views);

        view = new Node(MySQLConstants.DBEntity.VIEW);
        views.addChild(view);
        view.getAttrs().put("NAME", "actor_info");
    }

    @Test
    public void loadElement() throws SQLException {
        System.out.println(loader.loadElement(view));
    }

    @Test
    public void lazyChildrenLoad() throws SQLException {
        System.out.println(loader.lazyChildrenLoad(view));
    }

    @Test
    public void fullLoadElement() throws SQLException {
        System.out.println(loader.fullLoadElement(schema));
    }

    @Test
    public void fullLoadElement2() throws SQLException {
        System.out.println(loader.fullLoadElement(views));
    }

    @Test
    public void fullLoadElement3() throws SQLException {
        System.out.println(loader.fullLoadElement(view));
    }

    @Test
    public void loadCategory() throws SQLException {
        System.out.println(loader.loadCategory(views));
    }

    @Test
    public void loadCategory2() throws SQLException {
        System.out.println(loader.loadCategory(schema));
    }
}