package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.FUNCTION)
public class FunctionPrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();

        Map<String, String> attrs = node.getAttrs();

        Node paramsNode = node.wideSearch(MySQLConstants.NodeNames.PARAMETERS);

        sb.append(MySQLConstants.Delimiters.NEW_DELIMITER)
                .append(System.lineSeparator())
                .append("CREATE FUNCTION ")
                .append(attrs.get(MySQLConstants.AttributeName.NAME));

        if (paramsNode != null) {
            List<Node> params = paramsNode.getChildren();


            sb.append("(");
            for (int i = 1; i < params.size(); i++) {
                sb.append(params.get(i).getAttrs().get(MySQLConstants.AttributeName.NAME))
                        .append(" ")
                        .append(params.get(i).getAttrs().get(MySQLConstants.AttributeName.DTD_IDENTIFIER));
                if (!(i + 1 == params.size())) {
                    sb.append(MySQLConstants.Delimiters.COMA);
                }
            }
            sb.append(") RETURNS ")
                    .append(params.get(0).getAttrs().get(MySQLConstants.AttributeName.DTD_IDENTIFIER))
                    .append(System.lineSeparator());
            if ("YES".equals(attrs.get(MySQLConstants.AttributeName.IS_DETERMINISTIC))) {
                sb.append("DETERMINISTIC")
                        .append(System.lineSeparator());
            }
            sb.append(attrs.get(MySQLConstants.AttributeName.SQL_DATA_ACCESS))
                    .append(System.lineSeparator())
                    .append(attrs.get(MySQLConstants.AttributeName.ROUTINE_DEFINITION))
                    .append(MySQLConstants.Delimiters.DELIMITER)
                    .append(System.lineSeparator())
                    .append(MySQLConstants.Delimiters.OLD_DELIMITER);
        } else {
            sb.append(System.lineSeparator());
            sb.append(MySQLConstants.Delimiters.OLD_DELIMITER);
        }

        return sb.toString();
    }
}
