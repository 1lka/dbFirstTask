package com.dbbest.kirilenko.interactionWithDB.loaders.MySQLLoaders;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Connection4Test;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProcedureLoaderTest {

    @Test
    public void loadElement() throws SQLException {
        Node procedure = new Node(MySQLConstants.DBEntity.PROCEDURE);
        procedure.getAttrs().put("ROUTINE_SCHEMA", "sakila");
        procedure.getAttrs().put("ROUTINE_NAME", "film_in_stock");

        Loader procedureLoader = new ProcedureLoader(Connection4Test.getConnection());
        System.out.println(procedureLoader.loadElement(procedure));
    }
}