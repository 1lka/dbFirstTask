package com.dbbest.kirilenko.interactionWithDB.connections;

import com.dbbest.kirilenko.interactionWithDB.DBType;

public class ConnectFactory {

    /**
     * Factory method.
     *
     * @param type of database
     * @return connect for database
     */
    public static Connect getConnect(DBType type) {
        switch (type) {
            case MYSQL:
                return new MySQLConnect();
            default:
                throw new RuntimeException("App doesn't support such DB type");
        }
    }
}
