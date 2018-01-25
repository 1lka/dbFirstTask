package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class DeepSearchChainParser extends AbstractChainParser {

    public DeepSearchChainParser(ArgsParserManager manager) {
        super(manager);
        Option dOption = new Option("ds", true, "deep search using");
        dOption.setArgs(2);
        dOption.setOptionalArg(true);

        Options options = manager.getOptions();
        options.addOption(dOption);
    }

    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("ds")) {
            String[] params = cmd.getOptionValues("ds");
            if (params == null) {
                throw new ArgsInputException("search params required");
            }
            System.out.println(manager.getRoot().deepSearch(params));
        }
        next();
    }
}
