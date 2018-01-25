package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.*;

public class SearchChainParser extends AbstractChainParser {

    public SearchChainParser(ArgsParserManager manager) {
        super(manager);
        Option wOption = new Option("ws", true, "wide search using");
        wOption.setArgs(2);
        wOption.setOptionalArg(true);

        Option dOption = new Option("ds", true, "deep search using");
        dOption.setArgs(2);
        dOption.setOptionalArg(true);

        Options options = manager.getOptions();
        options.addOption(wOption);
        options.addOption(dOption);
    }

    @Override
    public void doWork() {
        String[] args = manager.getArgs();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(manager.getOptions(), args);
        } catch (ParseException e) {
            throw new ArgsInputException("wrong args", e);
        }

        //todo simplify this shit

        Node root = manager.getRoot();

        if (cmd.hasOption("ws")) {
            String[] params = cmd.getOptionValues("ws");

            if (params == null) {
                throw new ArgsInputException("search params required");
            }

            switch (params.length) {
                case 1:
                    System.out.println(manager.getRoot().wideSearch(params[0]));
                    break;
                case 2:
                    System.out.println(manager.getRoot().wideSearch(params[0], params[1]));
                    break;
            }
        }

        if (cmd.hasOption("ds")) {
            String[] params = cmd.getOptionValues("ds");

            if (params == null) {
                throw new ArgsInputException("search params required");
            }

            switch (params.length) {
                case 1:
                    System.out.println(manager.getRoot().deepSearch(params[0]));
                    break;
                case 2:
                    System.out.println(manager.getRoot().deepSearch(params[0], params[1]));
                    break;
            }
        }

    }
}
