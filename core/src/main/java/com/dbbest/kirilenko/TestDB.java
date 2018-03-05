package com.dbbest.kirilenko;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;

public class TestDB {

    public static void main(String[] args) throws SerializationException {

        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";
        String schema = "sakila";

        LoaderManager manager = new LoaderManager(type, url, login, pass);



//        XMLStrategyImpl strategy = new XMLStrategyImpl();
//        strategy.serialize(root, "tmp.xml");
    }


}
