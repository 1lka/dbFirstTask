package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.List;
import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.TRIGGER)
public class TriggerPrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        if (node == null) {
            return sb.toString();
        }

        if (node.getName().equals(MySQLConstants.DBEntity.TRIGGER)) {
            sb.append(MySQLConstants.Delimiters.NEW_DELIMITER).append(System.lineSeparator());
            Map<String, String> attrs = node.getAttrs();

            if (Boolean.valueOf(node.getAttrs().get(Loader.ELEMENT_LOADED))) {
                sb.append("CREATE TRIGGER ")
                        .append(attrs.get(MySQLConstants.AttributeName.NAME))
                        .append(" ")
                        .append(attrs.get(MySQLConstants.AttributeName.ACTION_TIMING))
                        .append(" ")
                        .append(attrs.get(MySQLConstants.AttributeName.EVENT_MANIPULATION))
                        .append(" ON ")
                        .append(attrs.get(MySQLConstants.AttributeName.EVENT_OBJECT_TABLE))
                        .append(" FOR EACH ")
                        .append(attrs.get(MySQLConstants.AttributeName.ACTION_ORIENTATION))
                        .append(System.lineSeparator())
                        .append(attrs.get(MySQLConstants.AttributeName.ACTION_STATEMENT))
                        .append(MySQLConstants.Delimiters.DELIMITER)
                        .append(System.lineSeparator());
                sb.append(MySQLConstants.Delimiters.OLD_DELIMITER);
            } else {
                sb.append("CREATE TRIGGER ")
                        .append(attrs.get(MySQLConstants.AttributeName.NAME))
                        .append(System.lineSeparator())
                        .append(MySQLConstants.Delimiters.OLD_DELIMITER);
            }
        } else {
            List<Node> triggers = node.getChildren();
            if (triggers.size() > 0) {
                sb.append(MySQLConstants.Delimiters.NEW_DELIMITER);
                for (Node trigger : triggers) {
                    Map<String, String> attrs = trigger.getAttrs();

                    sb.append(System.lineSeparator())
                            .append("CREATE TRIGGER ")
                            .append(attrs.get(MySQLConstants.AttributeName.NAME))
                            .append(" ")
                            .append(attrs.get(MySQLConstants.AttributeName.ACTION_TIMING))
                            .append(" ")
                            .append(attrs.get(MySQLConstants.AttributeName.EVENT_MANIPULATION))
                            .append(" ON ")
                            .append(attrs.get(MySQLConstants.AttributeName.EVENT_OBJECT_TABLE))
                            .append(" FOR EACH ")
                            .append(attrs.get(MySQLConstants.AttributeName.ACTION_ORIENTATION))
                            .append(System.lineSeparator())
                            .append(attrs.get(MySQLConstants.AttributeName.ACTION_STATEMENT))
                            .append(MySQLConstants.Delimiters.DELIMITER)
                            .append(System.lineSeparator());
                }
                sb.append(MySQLConstants.Delimiters.OLD_DELIMITER);
            }
        }
        return sb.toString();
    }
}
