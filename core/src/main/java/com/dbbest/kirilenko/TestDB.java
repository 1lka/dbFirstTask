package com.dbbest.kirilenko;

import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDB {

    public static void main(String[] args) throws SerializationException, SQLException {
        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";

        Node root = new Node(MySQLConstants.DBEntity.SCHEMA);
        Node root2 = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.SCHEMA_NAME, schemaName);
        Map<String, String> attrs2 = new HashMap<>(attrs);

        root.setAttrs(attrs);
        root2.setAttrs(attrs2);

        LoaderManager manager = LoaderManager.getInstance(type,schemaName, url, login, pass);
        manager.lazyChildrenLoad(root);
        manager.fullLoadElement(root);

        PrinterManager printerManager = new PrinterManager(type);
        System.out.println(printerManager.printDDL(root));

        XMLStrategyImpl strategy = new XMLStrategyImpl();
        strategy.serialize(root, "afterRefactoring.xml");
    }
}
