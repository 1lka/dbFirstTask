package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.exceptions.LoaderException;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LoaderManager {

    private DBType type;

    private Connection connection;

    private final Map<String, Loader> loaders;

    public LoaderManager(DBType type, String url, String login, String pass) {
        this.type = type;
        initConnection(url, login, pass);
        loaders = ReflectionUtil.obtain(type, EntityLoader.class);
    }

    private void initConnection(String dbURL, String login, String pass) {
        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(dbURL, login, pass);
            connection = connect.getConnection();
        } catch (SQLException e) {
            throw new LoaderException("can't obtain connection to DB: " + dbURL, e);
        }
    }


}
