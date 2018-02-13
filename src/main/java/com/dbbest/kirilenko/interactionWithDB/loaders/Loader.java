package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.Tree.Node;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Loader {


    public abstract Node load(Connection connection) throws SQLException;
}
