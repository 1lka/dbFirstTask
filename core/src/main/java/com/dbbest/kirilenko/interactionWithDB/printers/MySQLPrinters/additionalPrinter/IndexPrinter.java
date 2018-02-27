package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> indexes = new ArrayList<>(node.getChildren());

        for (Node index : indexes) {
            Map<String, String> attrs = index.getAttrs();

            if ("0".equals(attrs.get(Constants.NON_UNIQUE))) {
                sb.append("UNIQUE KEY (")
                        .append(attrs.get(Constants.COLUMNS_NAME))
                        .append("),")
                        .append(System.lineSeparator());
                continue;
            }

            String indexType = attrs.get(Constants.INDEX_TYPE);
            sb.append("BTREE".equals(indexType) ? "" : indexType + " ")
                    .append("KEY ")
                    .append(attrs.get(Constants.INDEX_NAME))
                    .append(" (")
                    .append(attrs.get(Constants.COLUMNS_NAME))
                    .append("),")
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
