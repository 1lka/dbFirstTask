package com.dbbest.kirilenko.interactionWithDB.connections;

import com.dbbest.kirilenko.interactionWithDB.DBType;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Connect {

    /**
     * connection for database
     */
    private Connection connection;

    private DBType type;

    private String dbName;
    private String url;
    private String login;
    private String password;

    public DBType getType() {
        return type;
    }

    public void setType(DBType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * method initialize connection for DB
     *
     * @param dbURL    url of DB.
     * @param login    DB login
     * @param password DB password
     * @throws SQLException if credentials are invalid
     */
    public void initConnection(String dbURL, String login, String password) throws SQLException {
        this.url = dbURL;
        this.login = login;
        this.password = password;
    }
}
