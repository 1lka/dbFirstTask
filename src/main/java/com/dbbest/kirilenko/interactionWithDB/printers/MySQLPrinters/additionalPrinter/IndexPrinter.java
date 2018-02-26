package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.ChildrenList;
import com.dbbest.kirilenko.Tree.Node;

import java.util.*;

public class IndexPrinter implements AdditionalPrinter {

    @Override
    public StringBuilder printElements(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> indexes = new ArrayList<>(node.getChildren());

        for (Node index : indexes) {
            Map<String, String> attrs = index.getAttrs();

            if ("0".equals(attrs.get("NON_UNIQUE"))) {
                sb.append("UNIQUE KEY (")
                        .append(attrs.get("COLUMNS_NAME"))
                        .append("),")
                        .append(System.lineSeparator());
                continue;
            }

            String indexType = attrs.get("INDEX_TYPE");
            sb.append("BTREE".equals(indexType) ? "" : indexType + " ")
                    .append("KEY ")
                    .append(attrs.get("INDEX_NAME"))
                    .append(" (")
                    .append(attrs.get("COLUMNS_NAME"))
                    .append("),")
                    .append(System.lineSeparator());

        }
        return sb;
    }
}
