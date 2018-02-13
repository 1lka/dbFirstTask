package com.dbbest.kirilenko.interactionWithDB.Connections;

import com.dbbest.kirilenko.interactionWithDB.DBType;

public class ConnectFactory {

    public static Connect getConnect(DBType type) {
        switch (type) {
            case MYSQL:
                return new MySQLConnect();
            default:
                throw new RuntimeException("App doesn't support such DB type");
        }
    }
}
