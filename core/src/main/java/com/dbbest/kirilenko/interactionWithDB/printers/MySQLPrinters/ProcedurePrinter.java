package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

@Print(element = DBElement.PROCEDURE)
public class ProcedurePrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        sb.append(Constants.NEW_DELIMITER)
                .append(System.lineSeparator())
                .append("CREATE PROCEDURE ")
                .append(attrs.get(Constants.ROUTINE_NAME))
                .append(" (");

        Node paramsNode = node.wideSearch(DBElement.PARAMETERS);
        List<Node> params = paramsNode.getChildren();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getAttrs().get(Constants.PARAMETER_MODE))
                    .append(" ")
                    .append(params.get(i).getAttrs().get(Constants.PARAMETER_NAME))
                    .append(" ")
                    .append(params.get(i).getAttrs().get(Constants.DTD_IDENTIFIER));

            if (!(i + 1 == params.size())) {
                sb.append(" ,");
            }
        }
        sb.append(")")
                .append(System.lineSeparator());

        if ("YES".equals(attrs.get(Constants.IS_DETERMINISTIC))) {
            sb.append("DETERMINISTIC")
                    .append(System.lineSeparator());
        }
        if (attrs.get(Constants.ROUTINE_COMMENT) != null) {
            sb.append("COMMENT '")
                    .append(attrs.get(Constants.ROUTINE_COMMENT))
                    .append("'")
                    .append(System.lineSeparator());
        }

        sb.append(attrs.get(Constants.SQL_DATA_ACCESS))
                .append(System.lineSeparator())
                .append(attrs.get(Constants.ROUTINE_DEFINITION))
                .append(Constants.DELIMITER)
                .append(System.lineSeparator())
                .append(Constants.OLD_DELIMITER);

        return sb.toString();
    }
}
