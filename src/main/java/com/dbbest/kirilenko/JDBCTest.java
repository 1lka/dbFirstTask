package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

public class JDBCTest {

    public static void main(String[] args) throws SerializationException {

        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/sakila?useSSL=false";
        String login = "root";
        String pass = "root";

        LoaderManager manager = new LoaderManager(type, url, login, pass);
        Node n = manager.lazyLoad();
        manager.fullLoadOnLazy(n);
        XMLStrategyImpl x = new XMLStrategyImpl();
        x.serialize(n,"tmp.xml");
//        System.out.println(n);
    }


}
