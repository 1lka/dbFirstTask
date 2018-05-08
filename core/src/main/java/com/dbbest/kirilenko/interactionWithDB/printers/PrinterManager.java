package com.dbbest.kirilenko.interactionWithDB.printers;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;

import java.util.Map;

public class PrinterManager {

    private final Map<String, Printer> printers;

    public PrinterManager(DBType type) {
        printers = ReflectionUtil.obtainAnnotatedClasses(type, NodePrinter.class);
    }

    public String printDDL(Node node) {
        String nodeName = node.getName();
        Printer printer = printers.get(nodeName);
        if (printer == null) {
            return "";
        }
        return printer.printElement(node);
    }

    public String printAllNodes(Node root) {
        return printAllNodes(root,new StringBuilder());
    }

    private String printAllNodes(Node node, StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(printDDL(node));
        sb.append(System.lineSeparator());

        StringBuilder finalSb = sb;
        node.getChildren().forEach(n -> printAllNodes(n, finalSb));

        return sb.toString();
    }
}
