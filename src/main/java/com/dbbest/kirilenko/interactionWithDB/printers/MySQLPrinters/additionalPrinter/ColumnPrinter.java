package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;

import java.util.List;
import java.util.Map;

public class ColumnPrinter implements AdditionalPrinter {

    @Override
    public StringBuilder printElements(Node node) {
        StringBuilder sb = new StringBuilder();

        List<Node> columns = node.getChildren();

        for (Node column : columns) {
            Map<String, String> attrs = column.getAttrs();
            sb.append(attrs.get("COLUMN_NAME"))
                    .append(" ")
                    .append(attrs.get("COLUMN_TYPE"));

            if ("NO".equals(attrs.get("IS_NULLABLE"))) {
                sb.append(" NOT NULL ");
            }
            if (attrs.get("COLUMN_DEFAULT") != null) {

                String columnDefault = attrs.get("COLUMN_DEFAULT");

                //todo костыль
                if (!"timestamp".equals(attrs.get("COLUMN_TYPE"))) {
                    try {
                        Double.parseDouble(columnDefault);
                    } catch (RuntimeException e) {
                        columnDefault = "'" + columnDefault + "'";
                    }
                }
                sb.append(" DEFAULT ")
                        .append(columnDefault)
                        .append(" ");
            }


            if (attrs.get("EXTRA") != null) {
                sb.append(attrs.get("EXTRA"));
            }
            sb.append(",")
                    .append(System.lineSeparator());
        }
        return sb;
    }
}
