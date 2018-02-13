package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;

import java.sql.Connection;

@Load(element = DBElement.PROCEDURE)
public class ProcedureLoader extends Loader {

    @Override
    public Node load(Connection connection) {
        return null;
    }
}
