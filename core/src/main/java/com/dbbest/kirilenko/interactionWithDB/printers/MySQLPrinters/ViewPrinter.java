package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@Print(element = DBElement.VIEW)
public class ViewPrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        sb.append("CREATE ");
        if ("INVOKER".equals(attrs.get(Constants.SECURITY_TYPE))) {
            sb.append("DEFINER=CURRENT_USER SQL SECURITY INVOKER ");
        }

        sb.append("VIEW ")
                .append(attrs.get(Constants.TABLE_NAME))
                .append(System.lineSeparator())
                .append("AS")
                .append(System.lineSeparator())
                .append(attrs.get(Constants.VIEW_DEFINITION))
                .append(";");

        return sb.toString();
    }
}
