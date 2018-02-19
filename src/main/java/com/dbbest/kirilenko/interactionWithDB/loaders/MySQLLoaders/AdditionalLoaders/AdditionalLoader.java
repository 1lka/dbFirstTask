package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders.AdditionalLoaders;

import com.dbbest.kirilenko.Tree.Node;

import java.sql.Connection;
import java.sql.SQLException;

public interface AdditionalLoader {

    static final String TABLE_NAME = "TABLE_NAME";

    public void load(Node node, Connection connection) throws SQLException;
}
