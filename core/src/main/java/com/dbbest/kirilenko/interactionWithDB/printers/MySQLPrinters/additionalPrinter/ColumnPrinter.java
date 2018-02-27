package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

public class ColumnPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();

        List<Node> columns = node.getChildren();
        for (Node column : columns) {
            Map<String, String> attrs = column.getAttrs();

            sb.append(attrs.get(Constants.COLUMN_NAME))
                    .append(" ")
                    .append(attrs.get(Constants.COLUMN_TYPE));

            if ("NO".equals(attrs.get(Constants.IS_NULLABLE))) {
                sb.append(" NOT NULL ");
            }

            String columnDefault = attrs.get(Constants.COLUMN_DEFAULT);
            if (columnDefault != null) {
                String dataType = attrs.get(Constants.DATA_TYPE);

                if ("varchar".equals(dataType) || "text".equals(dataType) || "enum".equals(dataType) || "set".equals(dataType)) {
                    columnDefault = "'" + columnDefault + "'";
                }
                sb.append(" DEFAULT ")
                        .append(columnDefault)
                        .append(" ");
            }
            if (attrs.get(Constants.EXTRA) != null) {
                sb.append(attrs.get(Constants.EXTRA));
            }
            sb.append(",")
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
