package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoaderPrinterManagerTest {

    private static LoaderManager loaderManager;
    private static PrinterManager printerManager;

    private static final String schema = "sakila";

    private static Node root;

    @BeforeClass
    public static void init() {
        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";
        loaderManager = new LoaderManager(type, url, login, pass);
        root = loaderManager.lazyDBLoad(schema);
        printerManager = new PrinterManager(type);
    }

    @Test
    public void loadTables() {
        Node tables = root.wideSearch(DBElement.TABLES);
        for (Node table : tables.getChildren()) {
            loaderManager.loadElement(table);
            System.out.println(printerManager.printDDL(table));
        }
    }

    @Test
    public void loadViews() {
        Node wiews = root.wideSearch(DBElement.VIEWS);
        for (Node view : wiews.getChildren()) {
            loaderManager.loadElement(view);
            System.out.println(printerManager.printDDL(view));
        }
    }

    @Test
    public void loadProcedures() {
        Node wiews = root.wideSearch(DBElement.PROCEDURES);
        for (Node view : wiews.getChildren()) {
            loaderManager.loadElement(view);
            System.out.println(printerManager.printDDL(view));

        }
    }

    @Test
    public void loadFunctions() {
        Node wiews = root.wideSearch(DBElement.FUNCTIONS);
        for (Node view : wiews.getChildren()) {
            loaderManager.loadElement(view);
            System.out.println(printerManager.printDDL(view));
        }
    }


}