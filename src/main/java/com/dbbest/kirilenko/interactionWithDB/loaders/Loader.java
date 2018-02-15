package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Loader {

    private String parent;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public abstract void lazyLoad(Node node,Connection connection) throws SQLException;
}
