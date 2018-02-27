package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.Tree.Node;

import java.util.List;
import java.util.Map;

public class TriggerPrinter implements AdditionalPrinter {
    @Override
    public StringBuilder printElements(Node node) {
        StringBuilder sb = new StringBuilder();
        List<Node> triggers = node.getChildren();
        if (triggers.size() > 0) {
            sb.append("DELIMITER ;;");
            for (Node trigger : triggers) {
                Map<String, String> attrs = trigger.getAttrs();

                sb.append(System.lineSeparator())
                        .append("CREATE TRIGGER ")
                        .append(attrs.get("TRIGGER_NAME"))
                        .append(" ")
                        .append(attrs.get("ACTION_TIMING"))
                        .append(" ")
                        .append(attrs.get("EVENT_MANIPULATION"))
                        .append(" ON ")
                        .append(attrs.get("EVENT_OBJECT_TABLE"))
                        .append(" FOR EACH ")
                        .append(attrs.get("ACTION_ORIENTATION"))
                        .append(System.lineSeparator())
                        .append(attrs.get("ACTION_STATEMENT"))
                        .append(";;")
                        .append(System.lineSeparator());
            }
            sb.append("DELIMITER ;");
        }
        return sb;
    }
}
