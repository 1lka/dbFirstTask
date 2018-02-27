package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.argsParser.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

public class DeepSearchChainParser extends AbstractChainParser {

    private static final Logger logger = Logger.getLogger(DeepSearchChainParser.class);

    /**
     * Instantiate a DeepSearchChainParser object
     *
     * @param manager object of ArgsParserManager for this chain
     */
    public DeepSearchChainParser(ArgsParserManager manager) {
        super(manager);
        Option dOption = new Option("ds", true, "deep search using");
        dOption.setArgs(2);
        dOption.setOptionalArg(true);

        Options options = manager.getOptions();
        options.addOption(dOption);
    }

    /**
     * Obtain command for deep search in tree, if command exists
     * obtain parameters for search.
     *
     * @throws ArgsInputException if no params for search.
     */
    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("ds")) {
            String[] params = cmd.getOptionValues("ds");

            if (params == null) {
                logger.error("wrong params for search");
                throw new ArgsInputException("wrong params");
            }

            Node found;
            switch (params.length) {
                case 1:
                    found = manager.getRoot().deepSearch(params[0]);
                    logger.debug("found node: " + found);
                    break;
                case 2:
                    found = manager.getRoot().deepSearch(params[0], params[1]);
                    logger.debug("found node: " + found);
                    break;
            }
        }
        next();
    }
}
