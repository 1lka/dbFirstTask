package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

public class ForeignKeyPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        if (node == null) {
            return sb.toString();
        }
        List<Node> fKeys = node.getChildren();

        for (Node fKey : fKeys) {
            Map<String, String> attrs = fKey.getAttrs();
            sb.append("CONSTRAINT ")
                    .append(attrs.get(MySQLConstants.AttributeName.NAME))
                    .append(" ")
                    .append("FOREIGN KEY")
                    .append(" (")
                    .append(attrs.get(MySQLConstants.AttributeName.NAME))
                    .append(") REFERENCES ")
                    .append(attrs.get(MySQLConstants.AttributeName.REFERENCED_TABLE_NAME))
                    .append(" (")
                    .append(attrs.get(MySQLConstants.AttributeName.REFERENCED_COLUMN_NAME))
                    .append(") ON DELETE ")
                    .append(attrs.get(MySQLConstants.AttributeName.DELETE_RULE))
                    .append(" ON UPDATE ")
                    .append(attrs.get(MySQLConstants.AttributeName.UPDATE_RULE))
                    .append(MySQLConstants.Delimiters.COMA)
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
