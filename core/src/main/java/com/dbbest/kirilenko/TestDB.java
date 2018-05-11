package com.dbbest.kirilenko;

import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
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

    public static void main(String[] args) throws SQLException {
        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost";
        String port = "3306";
        String login = "root";
        String pass = "root";

        Node root = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.NAME, schemaName);
        root.setAttrs(attrs);

        Connect connect = ConnectFactory.getConnect(type);
        connect.initConnection(url,port,login,pass);
        LoaderManager manager = new LoaderManager(connect);
        manager.lazyChildrenLoad(root);
        System.out.println(root + "\n");
        manager.fullLoadElement(root);

        PrinterManager printerManager = new PrinterManager(type);
        System.out.println(printerManager.printDDL(root));

        XMLStrategyImpl strategy = new XMLStrategyImpl();
//        strategy.serialize(root, "afterRefactoring.xml");
    }
}
