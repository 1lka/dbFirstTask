package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.PROCEDURE)
public class ProcedurePrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        sb.append(MySQLConstants.Delimiters.NEW_DELIMITER)
                .append(System.lineSeparator())
                .append("CREATE PROCEDURE ")
                .append(attrs.get(MySQLConstants.AttributeName.ROUTINE_NAME))
                .append(" (");

        Node paramsNode = node.wideSearch(MySQLConstants.NodeNames.PARAMETERS);
        List<Node> params = paramsNode.getChildren();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getAttrs().get(MySQLConstants.AttributeName.PARAMETER_MODE))
                    .append(" ")
                    .append(params.get(i).getAttrs().get(MySQLConstants.AttributeName.PARAMETER_NAME))
                    .append(" ")
                    .append(params.get(i).getAttrs().get(MySQLConstants.AttributeName.DTD_IDENTIFIER));

            if (!(i + 1 == params.size())) {
                sb.append(MySQLConstants.Delimiters.COMA);
            }
        }
        sb.append(")")
                .append(System.lineSeparator());

        if ("YES".equals(attrs.get(MySQLConstants.AttributeName.IS_DETERMINISTIC))) {
            sb.append("DETERMINISTIC")
                    .append(System.lineSeparator());
        }
        if (attrs.get(MySQLConstants.AttributeName.ROUTINE_COMMENT) != null) {
            sb.append("COMMENT '")
                    .append(attrs.get(MySQLConstants.AttributeName.ROUTINE_COMMENT))
                    .append("'")
                    .append(System.lineSeparator());
        }

        sb.append(attrs.get(MySQLConstants.AttributeName.SQL_DATA_ACCESS))
                .append(System.lineSeparator())
                .append(attrs.get(MySQLConstants.AttributeName.ROUTINE_DEFINITION))
                .append(MySQLConstants.Delimiters.DELIMITER)
                .append(System.lineSeparator())
                .append(MySQLConstants.Delimiters.OLD_DELIMITER);

        return sb.toString();
    }
}
