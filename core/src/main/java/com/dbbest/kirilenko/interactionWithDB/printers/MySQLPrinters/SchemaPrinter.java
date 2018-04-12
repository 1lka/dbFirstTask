package com.dbbest.kirilenko.interactionWithDB.printers.MySQLPrinters;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.util.Map;

@NodePrinter(element = MySQLConstants.DBEntity.SCHEMA)
public class SchemaPrinter extends Printer {

    @Override
    public String printElement(Node node) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> attrs = node.getAttrs();

        sb.append("DROP SCHEMA IF EXISTS ")
                .append(attrs.get(MySQLConstants.AttributeName.NAME))
                .append(MySQLConstants.Delimiters.SEMICOLON)
                .append(System.lineSeparator())
                .append("CREATE SCHEMA ")
                .append(attrs.get(MySQLConstants.AttributeName.NAME))
                .append(MySQLConstants.Delimiters.SEMICOLON)
                .append(System.lineSeparator());

        if (attrs.get(MySQLConstants.AttributeName.DEFAULT_CHARACTER_SET_NAME) != null) {
            sb.append("DEFAULT CHARACTER SET ")
                    .append(attrs.get(MySQLConstants.AttributeName.DEFAULT_CHARACTER_SET_NAME))
                    .append(MySQLConstants.Delimiters.SEMICOLON)
                    .append(System.lineSeparator())
                    .append("DEFAULT COLLATE ")
                    .append(attrs.get(MySQLConstants.AttributeName.DEFAULT_COLLATION_NAME))
                    .append(MySQLConstants.Delimiters.SEMICOLON)
                    .append(System.lineSeparator());
        }

        sb.append("USE ")
                .append(attrs.get(MySQLConstants.AttributeName.NAME))
                .append(MySQLConstants.Delimiters.SEMICOLON);

        return sb.toString();
    }
}
