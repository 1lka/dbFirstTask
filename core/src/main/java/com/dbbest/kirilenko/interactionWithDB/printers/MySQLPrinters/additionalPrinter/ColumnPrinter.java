package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
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

            sb.append(attrs.get(MySQLConstants.AttributeName.COLUMN_NAME))
                    .append(" ")
                    .append(attrs.get(MySQLConstants.AttributeName.COLUMN_TYPE));

            if ("NO".equals(attrs.get(MySQLConstants.AttributeName.IS_NULLABLE))) {
                sb.append(" NOT NULL");
            }

            String columnDefault = attrs.get(MySQLConstants.AttributeName.COLUMN_DEFAULT);
            if (columnDefault != null) {
                String dataType = attrs.get(MySQLConstants.AttributeName.DATA_TYPE);

                if ("varchar".equals(dataType) || "text".equals(dataType) || "enum".equals(dataType) || "set".equals(dataType)) {
                    columnDefault = "'" + columnDefault + "'";
                }
                sb.append(" DEFAULT ")
                        .append(columnDefault);
            }
            if (attrs.get(MySQLConstants.AttributeName.EXTRA) != null) {
                sb.append(" ")
                        .append(attrs.get(MySQLConstants.AttributeName.EXTRA));
            }
            sb.append(MySQLConstants.Delimiters.COMA)
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
