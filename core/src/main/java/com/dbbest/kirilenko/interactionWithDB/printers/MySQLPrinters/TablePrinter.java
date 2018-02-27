package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.Constants;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter.*;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@Print(element = DBElement.TABLE)
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

        String columns = columnsPrinter.printElement(node.wideSearch(DBElement.COLUMNS));
        String pKeys = pKeyPrinter.printElement(node.wideSearch(DBElement.PRIMARY_KEYS));
        String indexes = indexPrinter.printElement(node.wideSearch(DBElement.INDEXES));
        String fKeys = fKeyPrinter.printElement(node.wideSearch(DBElement.FOREIGN_KEYS));
        String triggers = triggerPrinter.printElement(node.wideSearch(DBElement.TRIGGERS));

        sb.append("CREATE TABLE ")
                .append(attrs.get(Constants.TABLE_NAME))
                .append("(")
                .append(System.lineSeparator())
                .append(columns)
                .append(pKeys)
                .append(System.lineSeparator())
                .append(indexes)
                .append(fKeys);

        int last = sb.lastIndexOf(",");
        if (last > 0) {
            sb = new StringBuilder(sb.substring(0, last));
        }

        sb.append(System.lineSeparator())
                .append(")ENGINE=")
                .append(attrs.get(Constants.ENGINE))
                .append(" DEFAULT COLLATE=")
                .append(attrs.get(Constants.TABLE_COLLATION))
                .append(";")
                .append(System.lineSeparator())
                .append(triggers);

        return sb.toString();
    }
}
