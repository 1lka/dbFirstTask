package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class WideSearchChainParser extends AbstractChainParser {

    public WideSearchChainParser(ArgsParserManager manager) {
        super(manager);
        Option wOption = new Option("ws", true, "wide search using");
        wOption.setArgs(2);
        wOption.setOptionalArg(true);

        Options options = manager.getOptions();
        options.addOption(wOption);
    }

    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("ws")) {
            String[] params = cmd.getOptionValues("ws");
            if (params == null) {
                throw new ArgsInputException("search params required");
            }
            System.out.println(manager.getRoot().wideSearch(params));
        }
        if (nextUnit != null) {
            nextUnit.doWork();
        }
    }
}
