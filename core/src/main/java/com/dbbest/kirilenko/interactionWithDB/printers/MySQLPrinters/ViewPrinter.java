package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.VIEW)
public class ViewPrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        if (Boolean.valueOf(node.getAttrs().get("fullyLoaded"))) {

            Map<String, String> attrs = node.getAttrs();

            sb.append("CREATE ");
            if ("INVOKER".equals(attrs.get(MySQLConstants.AttributeName.SECURITY_TYPE))) {
                sb.append("DEFINER=CURRENT_USER SQL SECURITY INVOKER ");
            }

            sb.append("VIEW ")
                    .append(attrs.get(MySQLConstants.AttributeName.NAME))
                    .append(System.lineSeparator())
                    .append("AS")
                    .append(System.lineSeparator())
                    .append(attrs.get(MySQLConstants.AttributeName.VIEW_DEFINITION))
                    .append(MySQLConstants.Delimiters.SEMICOLON);
        } else {
            sb.append("Fully load the node to see its DLL");
        }
        return sb.toString();
    }
}
