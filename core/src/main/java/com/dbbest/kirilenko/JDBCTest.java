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
        Node views = n.wideSearch(DBElement.VIEWS);
        Node procedures = n.wideSearch(DBElement.PROCEDURES);
//        System.out.println(printerManager.printDDL(tables.wideSearch("TABLE_NAME","film")));
        System.out.println(printerManager.printDDL(procedures.wideSearch("ROUTINE_NAME", "rewards_report")));
//        System.out.println(printerManager.printDDL(procedures.wideSearch("ROUTINE_NAME", "inventory_held_by_customer")));
//        System.out.println(printerManager.printDDL(procedures.wideSearch("ROUTINE_NAME", "inventory_in_stock")));

//        XMLStrategyImpl strategy = new XMLStrategyImpl();
//        strategy.serialize(n,"tmp.xml");


    }

    private static void fill(Node n, LoaderManager m) {
        for (Node node : n.getChildren()) {
            m.loadElement(node);
        }
    }
}
