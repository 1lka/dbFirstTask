package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;

import java.util.List;
import java.util.Map;

public class ForeignKeyPrinter implements AdditionalPrinter {
    @Override
    public StringBuilder printElements(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> fKeys = node.getChildren();

        for (Node fKey : fKeys) {
            Map<String, String> attrs = fKey.getAttrs();
            sb.append("CONSTRAINT ")
                    .append(attrs.get("CONSTRAINT_NAME"))
                    .append(" ")
                    .append("FOREIGN KEY")
                    .append(" (")
                    .append(attrs.get("COLUMN_NAME"))
                    .append(") REFERENCES ")
                    .append(attrs.get("REFERENCED_TABLE_NAME"))
                    .append(" (")
                    .append(attrs.get("REFERENCED_COLUMN_NAME"))
                    .append(") ON DELETE ")
                    .append(attrs.get("DELETE_RULE"))
                    .append(" ON UPDATE ")
                    .append(attrs.get("UPDATE_RULE"))
                    .append(",")
                    .append(System.lineSeparator());
        }
        return sb;
    }
}
