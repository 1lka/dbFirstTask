package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;

import java.util.List;
import java.util.Map;

public class PrimaryKeyPrinter implements AdditionalPrinter {

    @Override
    public StringBuilder printElements(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> pKeys = node.getChildren();
        if (pKeys.size() > 0) {
            sb.append("PRIMARY KEY (");
            for (Node pKey : pKeys) {
                Map<String, String> attrs = pKey.getAttrs();
                sb.append(attrs.get("COLUMN_NAME"))
                        .append(",");

            }
            int last = sb.lastIndexOf(",");
            if (last < 0) {
                return sb;
            }
            sb.append("),");
            sb = sb.deleteCharAt(last);
        }

        return sb;
    }
}
