package com.dbbest.kirilenko.model;

public class ConnectModel {

    private String url;
    private String port;

    public String getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public String getDb() {
        return db;
    }

    public String getLogin() {
        return login;
    }

    private String db;
    private String login;

    public ConnectModel(String url,String port, String dbName, String login) {
        this.port = port;
        this.url = url;
        this.db = dbName;
        this.login = login;
    }

    @Override
    public String toString() {
        return db;
    }
}
