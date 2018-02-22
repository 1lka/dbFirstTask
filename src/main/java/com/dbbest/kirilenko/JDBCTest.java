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

        Node table = n.deepSearch("TABLE_NAME", "film");
        manager.loadElement(table);

        Node procedure = n.wideSearch("procedure");
        manager.loadElement(procedure);

        Node function = n.wideSearch("function");
        manager.loadElement(function);


        XMLStrategyImpl x = new XMLStrategyImpl();
        x.serialize(n,"tmp.xml");
    }
}
