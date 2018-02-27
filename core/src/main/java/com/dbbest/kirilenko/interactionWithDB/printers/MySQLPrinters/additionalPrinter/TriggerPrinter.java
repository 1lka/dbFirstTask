package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

public class TriggerPrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> triggers = node.getChildren();
        if (triggers.size() > 0) {
            sb.append(Constants.NEW_DELIMITER);
            for (Node trigger : triggers) {
                Map<String, String> attrs = trigger.getAttrs();

                sb.append(System.lineSeparator())
                        .append("CREATE TRIGGER ")
                        .append(attrs.get(Constants.TRIGGER_NAME))
                        .append(" ")
                        .append(attrs.get(Constants.ACTION_TIMING))
                        .append(" ")
                        .append(attrs.get(Constants.EVENT_MANIPULATION))
                        .append(" ON ")
                        .append(attrs.get(Constants.EVENT_OBJECT_TABLE))
                        .append(" FOR EACH ")
                        .append(attrs.get(Constants.ACTION_ORIENTATION))
                        .append(System.lineSeparator())
                        .append(attrs.get(Constants.ACTION_STATEMENT))
                        .append(Constants.DELIMITER)
                        .append(System.lineSeparator());
            }
            sb.append(Constants.OLD_DELIMITER);
        }
        return sb.toString();
    }
}
