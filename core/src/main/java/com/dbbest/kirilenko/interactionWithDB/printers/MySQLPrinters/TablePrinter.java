package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter.*;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.TABLE)
public class TablePrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();

        Map<String, String> attrs = node.getAttrs();

        Printer columnsPrinter = new ColumnPrinter();
        Printer pKeyPrinter = new PrimaryKeyPrinter();
        Printer indexPrinter = new IndexPrinter();
        Printer fKeyPrinter = new ForeignKeyPrinter();
        Printer triggerPrinter = new TriggerPrinter();

        String columns = columnsPrinter.printElement(node.wideSearch(MySQLConstants.NodeNames.COLUMNS));
        String pKeys = pKeyPrinter.printElement(node.wideSearch(MySQLConstants.NodeNames.PRIMARY_KEYS));
        String indexes = indexPrinter.printElement(node.wideSearch(MySQLConstants.NodeNames.INDEXES));
        String fKeys = fKeyPrinter.printElement(node.wideSearch(MySQLConstants.NodeNames.FOREIGN_KEYS));
        String triggers = triggerPrinter.printElement(node.wideSearch(MySQLConstants.NodeNames.TRIGGERS));

        sb.append("CREATE TABLE ")
                .append(attrs.get(MySQLConstants.AttributeName.NAME))
                .append("(");

        if (Boolean.valueOf(node.getAttrs().get(Loader.FULLY_LOADED))) {
            sb.append(System.lineSeparator())
                    .append(columns)
                    .append(pKeys)
                    .append(System.lineSeparator())
                    .append(indexes)
                    .append(fKeys);
        }

        int last = sb.lastIndexOf(",");
        if (last > 0) {
            sb = new StringBuilder(sb.substring(0, last));
        }
        if (Boolean.valueOf(node.getAttrs().get(Loader.ELEMENT_LOADED))) {
            sb.append(System.lineSeparator())
                    .append(")ENGINE=")
                    .append(attrs.get(MySQLConstants.AttributeName.ENGINE))
                    .append(" DEFAULT COLLATE=")
                    .append(attrs.get(MySQLConstants.AttributeName.TABLE_COLLATION))
                    .append(MySQLConstants.Delimiters.SEMICOLON)
                    .append(System.lineSeparator())
                    .append(triggers);
        } else {
            sb.append(");");
        }


        return sb.toString();
    }
}
