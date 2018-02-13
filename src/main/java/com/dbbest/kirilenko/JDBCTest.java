package com.dbbest.kirilenko;

import com.dbbest.kirilenko.interactionWithDB.Connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.Connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoadersInitializer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class JDBCTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        DBType type = DBType.MYSQL;
        String url = "jdbc:mysql://localhost/sakila?useSSL=false";
        String name = "root";
        String pass = "root";

        LoadersInitializer initializer = new LoadersInitializer(type);
        Map<DBElement, Loader> elements = initializer.getLoaders();

        Connect connect = ConnectFactory.getConnect(type);
        connect.initConnection(url,name,pass);
    }


}
