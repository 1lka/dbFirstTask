package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

public class JDBCTest {

    public static void main(String[] args) throws SerializationException {

        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/?useSSL=false";
        String login = "root";
        String pass = "root";
        String schema = "sakila";

        LoaderManager manager = new LoaderManager(type, url, login, pass);
        Node n = manager.lazyDBLoad(schema);
        System.out.println(n);

        XMLStrategyImpl x = new XMLStrategyImpl();
        x.serialize(n,"tmp.xml");
    }


}
