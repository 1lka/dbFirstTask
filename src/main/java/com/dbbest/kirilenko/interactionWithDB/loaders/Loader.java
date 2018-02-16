package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Loader {

    private Class parent;

    public Class getParent() {
        return parent;
    }

    public void setParent(Class parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public abstract void lazyLoad(Node node, Connection connection) throws SQLException;

    public abstract void fullLoadOnLazy(Node node, Connection connection) throws SQLException;

    public abstract Node fullLoad(Connection connection) throws SQLException;
}
