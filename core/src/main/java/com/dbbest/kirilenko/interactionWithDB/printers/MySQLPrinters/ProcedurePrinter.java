package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
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

        sb.append("DELIMITER $$")
                .append(System.lineSeparator())
                .append("CREATE PROCEDURE ")
                .append(attrs.get("ROUTINE_NAME"))
                .append(" (");

        Node paramsNode = node.wideSearch(DBElement.PARAMETERS);
        List<Node> params = paramsNode.getChildren();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getAttrs().get("PARAMETER_MODE"))
                    .append(" ")
                    .append(params.get(i).getAttrs().get("PARAMETER_NAME"))
                    .append(" ")
                    .append(params.get(i).getAttrs().get("DTD_IDENTIFIER"));
            if (!(i + 1 == params.size())) {
                sb.append(" ,");
            }
        }
        sb.append(")")
                .append(System.lineSeparator());

        if ("YES".equals(attrs.get("IS_DETERMINISTIC"))) {
            sb.append("DETERMINISTIC")
                    .append(System.lineSeparator());
        }
        if (attrs.get("ROUTINE_COMMENT") != null) {
            sb.append("COMMENT '")
                    .append(attrs.get("ROUTINE_COMMENT"))
                    .append("'")
                    .append(System.lineSeparator());
        }

        sb.append(attrs.get("SQL_DATA_ACCESS"))
                .append(System.lineSeparator())
                .append(attrs.get("ROUTINE_DEFINITION"))
                .append(" $$")
                .append(System.lineSeparator())
                .append("DELIMITER ;");


        return sb.toString();
    }
}
