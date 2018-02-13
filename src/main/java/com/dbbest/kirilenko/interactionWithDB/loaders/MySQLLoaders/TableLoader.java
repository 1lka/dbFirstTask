package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.Connection;

@Load(element = DBElement.TABLE)
public class TableLoader extends Loader {

    @Override
    public Node load(Connection connection) {
        return null;
    }
}
