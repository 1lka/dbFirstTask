package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

public class ForeignKeyPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> fKeys = node.getChildren();

        for (Node fKey : fKeys) {
            Map<String, String> attrs = fKey.getAttrs();
            sb.append("CONSTRAINT ")
                    .append(attrs.get(Constants.CONSTRAINT_NAME))
                    .append(" ")
                    .append("FOREIGN KEY")
                    .append(" (")
                    .append(attrs.get(Constants.COLUMN_NAME))
                    .append(") REFERENCES ")
                    .append(attrs.get(Constants.REFERENCED_TABLE_NAME))
                    .append(" (")
                    .append(attrs.get(Constants.REFERENCED_COLUMN_NAME))
                    .append(") ON DELETE ")
                    .append(attrs.get(Constants.DELETE_RULE))
                    .append(" ON UPDATE ")
                    .append(attrs.get(Constants.UPDATE_RULE))
                    .append(",")
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
