package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

public class DeepSearchChainParser extends AbstractChainParser {

    private static final Logger logger = Logger.getLogger(DeepSearchChainParser.class);

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
                logger.error("search params required");
                throw new ArgsInputException("search params required");
            }
            Node found = manager.getRoot().deepSearch(params);
            logger.debug("found node: " + found);
        }
        next();
    }
}
