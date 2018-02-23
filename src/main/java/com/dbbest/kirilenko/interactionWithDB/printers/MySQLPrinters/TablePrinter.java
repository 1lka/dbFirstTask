package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.interactionWithDB.DBElement;
import com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters.additionalPrinter.ColumnPrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@Print(element = DBElement.TABLE)
public class TablePrinter extends Printer {
    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        ColumnPrinter printer = new ColumnPrinter();
        Node column = node.wideSearch(DBElement.COLUMNS);
        Node c = column.getChildren().get(0);
        System.out.println(printer.printElement(c));

        sb.append("CREATE TABLE ")
                .append(attrs.get("TABLE_NAME"))
                .append("(");


        sb.append(")ENGINE=")
                .append(attrs.get("ENGINE"))
                .append(" DEFAULT COLLATE=")
                .append(attrs.get("TABLE_COLLATION"));
        return sb.toString();
    }

    private String printChild(Node child) {


        return null;
    }
}
