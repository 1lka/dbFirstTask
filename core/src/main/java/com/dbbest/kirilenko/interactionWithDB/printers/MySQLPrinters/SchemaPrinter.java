package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@Print(element = DBElement.SCHEMA)
public class SchemaPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        sb.append("DROP SCHEMA IF EXISTS ")
                .append(attrs.get(Constants.SCHEMA_NAME))
                .append(";")
                .append(System.lineSeparator())
                .append("CREATE SCHEMA ")
                .append(attrs.get(Constants.SCHEMA_NAME))
                .append(";")
                .append(System.lineSeparator())
                .append("DEFAULT CHARACTER SET ")
                .append(attrs.get(Constants.DEFAULT_CHARACTER_SET_NAME))
                .append(";")
                .append(System.lineSeparator())
                .append("DEFAULT COLLATE ")
                .append(attrs.get(Constants.DEFAULT_COLLATION_NAME))
                .append(";")
                .append(System.lineSeparator())
                .append("USE ")
                .append(attrs.get(Constants.SCHEMA_NAME))
                .append(";");
        return sb.toString();
    }
}
