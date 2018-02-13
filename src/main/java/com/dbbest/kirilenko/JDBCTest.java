package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.LoaderManager;

public class JDBCTest {

    public static void main(String[] args) {

        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/sakila?useSSL=false";
        String login = "root";
        String pass = "root";

        LoaderManager manager = new LoaderManager(type, url, login, pass);
        Node n = manager.loadRoot();
        System.out.println(n);
    }


}
