package com.dbbest.kirilenko.interactionWithDB.printers;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.reflectionUtil.ReflectionUtil;

import java.util.Map;

public class PrinterManager {

    private final Map<String, Printer> printers;

    public PrinterManager(DBType type) {
        printers = ReflectionUtil.obtainMap(type, NodePrinter.class);
    }

    public String printDDL(Node node) {
        String nodeName = node.getName();
        Printer printer = printers.get(nodeName);
        if (printer == null) {
            return "can't print this node";
        }
        return printer.printElement(node);
    }
}
