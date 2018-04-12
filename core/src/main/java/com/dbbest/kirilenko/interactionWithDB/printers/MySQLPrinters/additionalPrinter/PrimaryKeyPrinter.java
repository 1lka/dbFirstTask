package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

public class PrimaryKeyPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        if (node == null) {
            return sb.toString();
        }
        List<Node> pKeys = node.getChildren();
        if (pKeys.size() > 0) {
            sb.append("PRIMARY KEY (");
            for (Node pKey : pKeys) {
                Map<String, String> attrs = pKey.getAttrs();
                sb.append(attrs.get(MySQLConstants.AttributeName.NAME))
                        .append(MySQLConstants.Delimiters.COMA);
            }
            int last = sb.lastIndexOf(",");
            if (last < 0) {
                return sb.toString();
            }
            sb.append("),");
            sb = sb.deleteCharAt(last);
        }
        return sb.toString();
    }
}

