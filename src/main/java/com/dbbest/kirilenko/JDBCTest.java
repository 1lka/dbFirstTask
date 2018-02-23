package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.SchemaPrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.List;

public class JDBCTest {

    public static void main(String[] args) throws SerializationException {

        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";
        String schema = "sakila";

        LoaderManager manager = new LoaderManager(type, url, login, pass);
        Node n = manager.lazyDBLoad(schema);
        List<Node> list = n.getChildren();
        for (Node node : list) {
            fill(node, manager);
        }

        PrinterManager printerManager = new PrinterManager(type);
        Node tables = n.wideSearch(DBElement.TABLES);
        System.out.println(printerManager.printDDL(tables.getChildren().get(0)));

        XMLStrategyImpl strategy = new XMLStrategyImpl();
        strategy.serialize(n,"tmp.xml");
    }

    private static void fill(Node n, LoaderManager m) {
        for (Node node : n.getChildren()) {
            m.loadElement(node);
        }
    }
}
