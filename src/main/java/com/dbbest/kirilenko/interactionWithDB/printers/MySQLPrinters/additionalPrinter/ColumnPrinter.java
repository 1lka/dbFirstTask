package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;

import java.util.Map;

public class ColumnPrinter implements AdditionalPrinter {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();
        sb.append(attrs.get("COLUMN_NAME"))
                .append(" ")
                .append(attrs.get("COLUMN_TYPE"));

        if ("NO".equals(attrs.get("IS_NULLABLE"))) {
            sb.append(" NOT NULL ");
        }

        if (attrs.get("EXTRA") != null) {
            sb.append(attrs.get("EXTRA"));
        }
        sb.append(",");
        return sb.toString();
    }
}
