package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
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

        AdditionalPrinter columnsPrinter = new ColumnPrinter();
        AdditionalPrinter pKeyPrinter = new PrimaryKeyPrinter();
        AdditionalPrinter indexPrinter = new IndexPrinter();
        AdditionalPrinter fKeyPrinter = new ForeignKeyPrinter();
        AdditionalPrinter triggerPrinter = new TriggerPrinter();

        sb.append("CREATE TABLE ")
                .append(attrs.get("TABLE_NAME"))
                .append("(")
                .append(System.lineSeparator());

        StringBuilder columns = columnsPrinter.printElements(node.wideSearch(DBElement.COLUMNS));
        sb.append(columns);

        StringBuilder pKeys = pKeyPrinter.printElements(node.wideSearch(DBElement.PRIMARY_KEYS));
        sb.append(pKeys)
                .append(System.lineSeparator());

        StringBuilder indexes = indexPrinter.printElements(node.wideSearch(DBElement.INDEXES));
        sb.append(indexes);

        StringBuilder fKeys = fKeyPrinter.printElements(node.wideSearch(DBElement.FOREIGN_KEYS));
        sb.append(fKeys);

        int last = sb.lastIndexOf(",");
        if (last > 0) {
            sb = new StringBuilder(sb.substring(0, last));
        }

        sb.append(System.lineSeparator())
                .append(")ENGINE=")
                .append(attrs.get("ENGINE"))
                .append(" DEFAULT COLLATE=")
                .append(attrs.get("TABLE_COLLATION"))
                .append(";")
                .append(System.lineSeparator());

        StringBuilder triggers = triggerPrinter.printElements(node.wideSearch(DBElement.TRIGGERS));
        sb.append(triggers);

        return sb.toString();
    }
}
